package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.model.response.NoteResponse;
import com.github.remusselea.scentdb.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scentdb/v1")
@Slf4j
public class NoteController {

  private NoteService noteService;

  public NoteController(NoteService noteService) {
    this.noteService = noteService;
  }

  /**
   * Gets all existing notes from the service and returns them.
   * @return a {@link NoteResponse} with all the existing notes.
   */
  @GetMapping("/notes")
  public NoteResponse getAllNotes() {
    log.info("Getting all notes");
    return noteService.getAllNotes();
  }

}
