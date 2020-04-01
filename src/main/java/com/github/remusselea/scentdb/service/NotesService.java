package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.data.Note;
import com.github.remusselea.scentdb.data.repo.NoteRepository;
import com.github.remusselea.scentdb.mapping.NoteMapper;
import com.github.remusselea.scentdb.model.response.NoteResponse;
import com.github.remusselea.scentdb.model.response.note.NoteDto;
import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class NotesService {

  private NoteRepository noteRepository;

  private NoteMapper noteMapper;

  public NotesService(NoteRepository noteRepository, NoteMapper noteMapper) {
    this.noteRepository = noteRepository;
    this.noteMapper = noteMapper;
  }

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
