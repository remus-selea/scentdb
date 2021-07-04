package com.github.remusselea.scentdb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.request.NoteRequest;
import com.github.remusselea.scentdb.dto.response.NoteResponse;
import com.github.remusselea.scentdb.service.NoteService;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/scentdb/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class NoteController {

  private NoteService noteService;


  public NoteController(NoteService noteService) {
    this.noteService = noteService;
  }

  /**
   * Gets all existing notes from the service and returns them.
   *
   * @return a {@link NoteResponse} with all the existing notes.
   */
  @GetMapping("/notes")
  public List<NoteDto> getAllNotes() {
    log.info("Getting all notes");
    return noteService.getAllNotes();
  }

  /**
   * Gets all existing notes from the service and returns them.
   *
   * @return a {@link NoteResponse} with all the existing notes.
   */
  @GetMapping("/notes/{noteId}")
  public NoteResponse getNoteById(@PathVariable Long noteId) {
    log.info("Getting note by Id");
    return noteService.getNoteById(noteId);
  }

  /**
   * Saves a note.
   *
   * @param note the note request body.
   * @return the saved note.
   */
  @PostMapping("/notes")
  public NoteResponse saveNote(@RequestParam("image") MultipartFile file,
      @RequestParam("note") String note) {
    log.info("Saving a note");
    NoteRequest noteRequest = deserializeStringToNoteRequest(note);

    return noteService.saveNote(noteRequest, file);
  }

  /**
   * Updates a note.
   *
   * @param note the note request body.
   * @return the updated note.
   */
  @PutMapping("/notes/{noteId}")
  public NoteResponse updateNote(@RequestParam("image") MultipartFile file,
      @RequestParam("note") String note, @PathVariable Long noteId) {
    log.info("Updating a note");
    NoteRequest noteRequestDto = deserializeStringToNoteRequest(note);

    if (Objects.nonNull(noteRequestDto)) {
      noteRequestDto.setNoteId(noteId);
    }

    return noteService.saveNote(noteRequestDto, file);
  }

  @DeleteMapping("/notes/{noteId}")
  public void removeNote(@PathVariable Long noteId) {

    noteService.removeNoteById(noteId);
  }


  @GetMapping("/notes/search")
  public Page searchNotes(@RequestParam(value = "q", required = false) String query,
      @PageableDefault(size = 9) Pageable pageable) {
    log.info("searching notes");

    return noteService.search(pageable, query);
  }

  private NoteRequest deserializeStringToNoteRequest(String note) {
    NoteRequest noteRequest = null;
    try {
      noteRequest = new ObjectMapper().readValue(note, NoteRequest.class);
    } catch (JsonProcessingException e) {
      log.error("Could not parse note string object, cause {0}", e);
    }
    return noteRequest;
  }


}
