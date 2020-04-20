package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.data.Note;
import com.github.remusselea.scentdb.data.repo.NoteRepository;
import com.github.remusselea.scentdb.mapping.NoteMapper;
import com.github.remusselea.scentdb.model.note.NoteDto;
import com.github.remusselea.scentdb.model.response.NoteResponse;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

  private NoteRepository noteRepository;

  private NoteMapper noteMapper;

  public NoteService(NoteRepository noteRepository, NoteMapper noteMapper) {
    this.noteRepository = noteRepository;
    this.noteMapper = noteMapper;
  }

  /**
   * Gets all the existing notes from the configured {@link NoteRepository}.
   * @return a {@link NoteResponse} with all the existing notes.
   */
  @Transactional
  public NoteResponse getAllNotes() {
    Iterable<Note> notes = noteRepository.findAll();
    NoteResponse noteResponse = new NoteResponse();

    Map<Long, NoteDto> noteDtoMap = new HashMap<>();

    notes.forEach(note -> {
      NoteDto noteDto = noteMapper.noteToNoteDto(note);
      noteDtoMap.put(note.getNoteId(), noteDto);
    });
    noteResponse.setNoteDtoMap(noteDtoMap);

    return noteResponse;
  }

}
