package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.dto.mapper.NoteMapper;
import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.request.NoteRequest;
import com.github.remusselea.scentdb.dto.response.NoteResponse;
import com.github.remusselea.scentdb.exception.FileStorageException;
import com.github.remusselea.scentdb.model.entity.Note;
import com.github.remusselea.scentdb.model.repo.NoteRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Slf4j
public class NoteService {

  private NoteRepository noteRepository;

  private NoteMapper noteMapper;

  @Value("${notes.images.dir:${user.home}}")
  public String uploadDir;

  public NoteService(NoteRepository noteRepository, NoteMapper noteMapper) {
    this.noteRepository = noteRepository;
    this.noteMapper = noteMapper;
  }

  /**
   * Gets all the existing notes from the configured {@link NoteRepository}.
   *
   * @return a {@link NoteResponse} with all the existing notes.
   */
  @Transactional
  public NoteResponse getAllNotes() {
    log.debug("Getting all notes from the database");
    Iterable<Note> notes = noteRepository.findAll();
    Map<Long, NoteDto> noteDtoMap = new HashMap<>();

    notes.forEach(note -> {
      NoteDto noteDto = noteMapper.noteToNoteDto(note);
      noteDtoMap.put(note.getNoteId(), noteDto);
    });

    NoteResponse noteResponse = new NoteResponse();
    noteResponse.setNoteDtoMap(noteDtoMap);

    return noteResponse;
  }

  /**
   * Get a note by Id from the configured {@link NoteRepository}.
   *
   * @param noteId the identifier of the note.
   * @return a {@link NoteResponse} with the found note.
   */
  public NoteResponse getNoteById(Long noteId) {
    log.debug("Getting note from the database by Id {}", noteId);
    Optional<Note> noteOptional = noteRepository.findById(noteId);
    Map<Long, NoteDto> noteDtoMap = new HashMap<>();

    if (noteOptional.isPresent()) {
      Note note = noteOptional.get();
      NoteDto noteDto = noteMapper.noteToNoteDto(note);
      noteDtoMap.put(note.getNoteId(), noteDto);
    }
    NoteResponse noteResponse = new NoteResponse();
    noteResponse.setNoteDtoMap(noteDtoMap);

    return noteResponse;
  }

  /**
   * Creates or updates a {@link Note} in the configured {@link NoteRepository}.
   *
   * @param noteRequest the object containing the data of a note to be created or updated.
   * @return returns a NoteResponse containing the note saved in the database.
   */
  public NoteResponse saveNote(NoteRequest noteRequest,
      MultipartFile file) {

    String fileName = storeImage(file);
    String noteImgPath = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/scentdb/v1/images/notes/")
        .path(fileName)
        .toUriString();

    noteRequest.setImgPath(noteImgPath);
    Note note = noteMapper.convertNoteRequestToNote(noteRequest);

    log.debug("Saving note into the database {}", note);
    Note savedNote = noteRepository.save(note);

    return createSavedNoteResponse(savedNote);
  }

  private NoteResponse createSavedNoteResponse(Note savedNote) {
    NoteDto savedNoteDto = noteMapper.noteToNoteDto(savedNote);

    Map<Long, NoteDto> noteDtoMap = new HashMap<>();
    noteDtoMap.put(savedNote.getNoteId(), savedNoteDto);

    NoteResponse noteResponse = new NoteResponse();
    noteResponse.setNoteDtoMap(noteDtoMap);
    return noteResponse;
  }

  public void removeNoteById(Long noteId) {
    noteRepository.deleteById(noteId);
  }


  private String storeImage(MultipartFile file) {
    // Normalize file name
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    try {
      // Copy file to the target location (Replacing existing file with the same name)
      Path targetLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
      Path copyLocation = targetLocation.resolve(fileName);
      Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex) {
      throw new FileStorageException("Could not store file " + fileName + ". Please try again!",
          ex);
    }
    return fileName;
  }


}
