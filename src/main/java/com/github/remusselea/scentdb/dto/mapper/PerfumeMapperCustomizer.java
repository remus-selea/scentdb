package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeNoteDto;
import com.github.remusselea.scentdb.dto.model.perfume.Type;
import com.github.remusselea.scentdb.dto.request.PerfumeRequest;
import com.github.remusselea.scentdb.model.entity.Note;
import com.github.remusselea.scentdb.model.entity.Perfume;
import com.github.remusselea.scentdb.model.entity.PerfumeNote;
import com.github.remusselea.scentdb.model.entity.Perfumer;
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

      perfumeNoteDtoMap.compute(noteType, (key, value) -> {
        PerfumeNoteDto perfumeNoteDto = value;
        if (value == null) {
          perfumeNoteDto = new PerfumeNoteDto();
          perfumeNoteDto.setNoteType(NOTE_TYPES_NAMES.get(key));
        }
        perfumeNoteDto.addNoteId(perfumeNote.getNote().getNoteId());
        return perfumeNoteDto;
      });
    }

    List<PerfumeNoteDto> perfumeNoteDtoList = new ArrayList<>(perfumeNoteDtoMap.values());

    perfumeDto.setPerfumeNoteDtoList(perfumeNoteDtoList);
    log.debug("Converted Perfume {} to PerfumeDto: {}", perfume, perfumeDto);
  }

  /**
   * Perform the custom mappings from {@link PerfumeRequest} to a {@link Perfume} object.
   */
  @AfterMapping
  public void afterMappingPerfumeRequestToPerfume(PerfumeRequest perfumeRequest,
      @MappingTarget Perfume perfume) {
    Map<Long, NoteDto> noteDtoMap = perfumeRequest.getNoteDtoMap();

    for (PerfumeNoteDto perfumeNoteDto : perfumeRequest.getPerfumeDto().getPerfumeNoteDtoList()) {
      for (Long noteId : perfumeNoteDto.getNotes()) {
        // create the note and set it's id
        Note note = new Note();
        note.setNoteId(noteId);
        // find the noteDto in map
        NoteDto noteDto = noteDtoMap.get(note.getNoteId());
        // set note name and img path
        note.setNoteName(noteDto.getNoteName());
        note.setImgPath(noteDto.getImgPath());
        // get PerfumeNote type
        String noteType = perfumeNoteDto.getNoteType();
        Character character = getKey(NOTE_TYPES_NAMES, noteType);
        // add the note and the type to the perfume
        perfume.addNote(note, character);
      }
    }
    log.debug("Converted PerfumeRequest: {} to Perfume: {}", perfumeRequest, perfume);
  }

  /**
   * Get the Key of a Map if it contains a value.
   *
   * @param map   the map which will be checked for a value.
   * @param value the value to check for in the map.
   * @return the key of the map if it is found or null if it is not found.
   */
  public static <K, V> K getKey(Map<K, V> map, V value) {
    return map.entrySet()
        .stream()
        .filter(entry -> value.equals(entry.getValue()))
        .map(Map.Entry::getKey)
        .findFirst().orElse(null);
  }
}