package com.github.remusselea.scentdb.mapping;

import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.data.PerfumeNote;
import com.github.remusselea.scentdb.model.response.note.NoteDto;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeWrapper;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoteMapperCustomizer {

  private NoteMapper noteMapper;

  public NoteMapperCustomizer(NoteMapper noteMapper) {
    this.noteMapper = noteMapper;
  }

  /**
   * Perform the custom mappings from {@link PerfumeWrapper} to a {@link Perfume} object.
   */
  @AfterMapping
  public void afterMappingPerfumeNotesToNoteDtoMap(@MappingTarget Map<Long, NoteDto> noteDtoMap,
                                                   Set<PerfumeNote> perfumeNotes) {

    for (PerfumeNote perfumeNote : perfumeNotes) {
      NoteDto noteDto = noteMapper.perfumeNoteToNoteDto(perfumeNote);

      for (Map.Entry<Long, NoteDto> map : noteDtoMap.entrySet()) {
        map.setValue(noteDto);
      }
    }

    log.debug("Converted perfumeNotes: {} to noteDtoMap: {}", perfumeNotes, noteDtoMap);
  }


}
