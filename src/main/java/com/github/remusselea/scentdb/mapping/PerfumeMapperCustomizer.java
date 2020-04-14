package com.github.remusselea.scentdb.mapping;

import com.github.remusselea.scentdb.data.Note;
import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.data.PerfumeNote;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeDto;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeNoteDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
   * Perform the custom mappings from {@link Perfume} to a {@link PerfumeDto} object.
   */
  @AfterMapping
  public void afterMappingPerfumeToPerfumeDto(Perfume perfume,
                                              @MappingTarget PerfumeDto perfumeDto) {
    if (perfume.getPerfumeNotes() == null) {
      log.warn("No perfume notes found for perfume {}", perfume);
    }
    Map<Character, PerfumeNoteDto> perfumeNoteDtoMap = new HashMap<>();

    for (PerfumeNote perfumeNote : perfume.getPerfumeNotes()) {
      Character noteType = perfumeNote.getNoteType();
      PerfumeNoteDto perfumeNoteDto = perfumeNoteDtoMap.get(noteType);

      if (perfumeNoteDto == null) {
        perfumeNoteDto = new PerfumeNoteDto();
        perfumeNoteDto.setPerfumeNoteId(perfumeNote.getPerfumeNoteId());

        perfumeNoteDto.setType(NOTE_TYPES_NAMES.get(noteType));
        perfumeNoteDtoMap.put(noteType, perfumeNoteDto);
      }
      perfumeNoteDto.addNote(perfumeNote.getNote().getNoteId());
    }

    List<PerfumeNoteDto> perfumeNoteDtoList = new ArrayList<>(perfumeNoteDtoMap.values());

    perfumeDto.setPerfumeNoteDtoList(perfumeNoteDtoList);
    log.debug("Converted Perfume {} to PerfumeDto: {}", perfume, perfumeDto);
  }


  /**
   * Perform the custom mappings from {@link PerfumeDto} to a {@link Perfume} object.
   */
  @AfterMapping
  public void afterMappingPerfumeDtoToPerfume(PerfumeDto perfumeDto,
                                              @MappingTarget Perfume perfume) {
    Set<PerfumeNote> perfumeNoteSet = new HashSet<>();

    for (PerfumeNoteDto perfumeNoteDto : perfumeDto.getPerfumeNoteDtoList()) {
      for (Long noteId : perfumeNoteDto.getNotes()) {
        // add note to PerfumeNote
        Note note = new Note();
        note.setNoteId(noteId);

        PerfumeNote perfumeNote = new PerfumeNote();
        perfumeNote.setNote(note);

        // set PerfumeNote type
        String noteType = perfumeNoteDto.getType();
        Character character = getKey(NOTE_TYPES_NAMES, noteType);
        perfumeNote.setNoteType(character);

        perfumeNoteSet.add(perfumeNote);
      }
    }

    perfume.setPerfumeNotes(perfumeNoteSet);

    log.debug("Converted PerfumeDto: {} to Perfume: {}", perfumeDto, perfume);
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


  public static <K, V> K getKey(Map<K, V> map, V value) {
    return map.entrySet()
        .stream()
        .filter(entry -> value.equals(entry.getValue()))
        .map(Map.Entry::getKey)
        .findFirst().get();
  }
}