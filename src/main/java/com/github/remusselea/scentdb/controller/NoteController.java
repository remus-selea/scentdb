package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.model.response.NoteResponse;
import com.github.remusselea.scentdb.service.NotesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scentdb/v1")
@Slf4j
public class NoteController {

  private NotesService notesService;

  public NoteController(NotesService notesService) {
    this.notesService = notesService;
  }

  @GetMapping("/notes")
  public NoteResponse getAllNotes() {
    log.info("Getting all notes");
    NoteResponse noteResponse = notesService.getAllNotes();
    return noteResponse;
  }

}
