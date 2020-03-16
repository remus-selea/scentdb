package com.github.remusselea.scentdb.mapping;

import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.data.PerfumeNote;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeNoteDto;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PerfumeMapperCustomizer {

  private static final Map<Character, String> NOTE_TYPES_NAMES = new HashMap<>();

  static {
    NOTE_TYPES_NAMES.put('t', "top notes");
    NOTE_TYPES_NAMES.put('b', "base notes");
    NOTE_TYPES_NAMES.put('m', "middle notes");
    NOTE_TYPES_NAMES.put('g', "general notes");
  }


  /**
   * Perform the custom mappings from {@link Perfume} to a {@link PerfumeWrapper} object.
   */
  @AfterMapping
  public void afterMappingPerfumeToPerfumeWrapper(Perfume perfume, @MappingTarget PerfumeWrapper perfumeWrapper) {
    if (perfume.getPerfumeNotes() == null) {
      log.warn("No perfume notes found for perfume {}", perfume);
    }
    Map<Character, PerfumeNoteDto> perfumeNoteDtoMap = new HashMap<>();

    for (PerfumeNote perfumeNote : perfume.getPerfumeNotes()) {
      Character noteType = perfumeNote.getNoteType();
      PerfumeNoteDto perfumeNoteDto = perfumeNoteDtoMap.get(noteType);

      if (perfumeNoteDto == null) {
        perfumeNoteDto = new PerfumeNoteDto();
        perfumeNoteDto.setType(NOTE_TYPES_NAMES.get(noteType));
        perfumeNoteDtoMap.put(noteType, perfumeNoteDto);
      }
      perfumeNoteDto.addNote(perfumeNote.getNote().getNoteId());
    }

    List<PerfumeNoteDto> perfumeNoteDtoList = new ArrayList<>(perfumeNoteDtoMap.values());

    perfumeWrapper.setPerfumeNoteDtos(perfumeNoteDtoList);
    log.debug("Converted perfume {} to perfumeWrapper: {}", perfume, perfumeWrapper);
  }


  /**
   * Perform the custom mappings from {@link PerfumeWrapper} to a {@link Perfume} object.
   */
  @AfterMapping
  public void afterMappingPerfumeWrapperToPerfume(PerfumeWrapper perfumeWrapper,
                                                  @MappingTarget Perfume perfume) {

    log.debug("Converted perfumeWrapper: {} to entity perfume: {}", perfumeWrapper, perfume);
  }

  /**
   * Set the value if the String is present.
   *
   * @param input the input to be checked
   * @return the value of the input or {@code null} if not present
   */
  private String getValueIfPresent(String input) {
    return input != null && !input.isBlank() ? input : null;
  }

}