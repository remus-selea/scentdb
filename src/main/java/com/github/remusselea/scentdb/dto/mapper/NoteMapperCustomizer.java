package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.model.entity.Perfume;
import com.github.remusselea.scentdb.model.entity.PerfumeNote;
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
   * Perform the custom mappings from {@link PerfumeDto} to a {@link Perfume} object.
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
