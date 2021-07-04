package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.dto.mapper.NoteMapper;
import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.dto.request.NoteRequest;
import com.github.remusselea.scentdb.dto.response.NoteResponse;
import com.github.remusselea.scentdb.exception.FileStorageException;
import com.github.remusselea.scentdb.exception.ScentdbBusinessUncheckedException;
import com.github.remusselea.scentdb.model.entity.Company;
import com.github.remusselea.scentdb.model.entity.Note;
import com.github.remusselea.scentdb.model.entity.Perfumer;
import com.github.remusselea.scentdb.repo.NoteRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.backend.elasticsearch.search.query.ElasticsearchSearchQuery;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

  private EntityManager entityManager;


  public NoteService(NoteRepository noteRepository,
      NoteMapper noteMapper, EntityManager entityManager) {
    this.noteRepository = noteRepository;
    this.noteMapper = noteMapper;
    this.entityManager = entityManager;
  }

  /**
   * Gets all the existing notes from the configured {@link NoteRepository}.
   *
   * @return a {@link NoteResponse} with all the existing notes.
   */
  @Transactional
  public List<NoteDto> getAllNotes() {
    log.debug("Getting all notes from the database");
    List<Note> notes = noteRepository.findAll();
    List<NoteDto> noteDtoList = new ArrayList<>(notes.size());

    notes.forEach(note -> {
      NoteDto noteDto = noteMapper.noteToNoteDto(note);
      noteDtoList.add(noteDto);
    });

    return noteDtoList;
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
    boolean noteExists = noteRepository.existsNoteByNoteName(noteRequest.getNoteName());

    if (noteExists) {
      log.error("A note with name : {} was already found to be existing in the database.",
          noteRequest.getNoteName());
      throw new ScentdbBusinessUncheckedException("Cannot save duplicate notes in the database");
    }

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

  @org.springframework.transaction.annotation.Transactional(readOnly = true)
  public Page<NoteDto> search(Pageable pageable, String query) {
    Page<NoteDto> noteDtoPage;

    if ((query == null || query.isBlank()) ) {
      noteDtoPage = searchMatchAll(pageable);
    } else {
      noteDtoPage = searchTerms(pageable, query);
    }

    return noteDtoPage;
  }

  @org.springframework.transaction.annotation.Transactional(readOnly = true)
  public Page<NoteDto> searchMatchAll(Pageable pageable) {
    SearchSession searchSession = Search.session(entityManager);

    ElasticsearchSearchQuery<Note> searchQuery = searchSession.search(Note.class)
        .extension(ElasticsearchExtension.get())
        .where(SearchPredicateFactory::matchAll).toQuery();

    SearchResult<Note> result = searchQuery
        .fetch((int) pageable.getOffset(), pageable.getPageSize());

    List<Note> noteList = result.hits();
    List<NoteDto> noteDtoList = new ArrayList<>(noteList.size());

    for (Note note : noteList) {
      NoteDto noteDto = noteMapper.noteToNoteDto(note);
      noteDtoList.add(noteDto);
    }

    return new PageImpl<>(noteDtoList, pageable, result.total().hitCount());
  }


  @org.springframework.transaction.annotation.Transactional(readOnly = true)
  public Page<NoteDto> searchTerms(Pageable pageable, String query) {
    SearchSession searchSession = Search.session(entityManager);

    SearchScope<Note> scope = searchSession.scope(Note.class);

    SearchPredicate boolPredicates = formSearchPredicates(query, scope);

    SearchResult<Note> result = searchSession.search(scope).where(boolPredicates)
        .fetch((int) pageable.getOffset(), pageable.getPageSize());

    List<Note> noteList = result.hits();
    List<NoteDto> noteDtoList = new ArrayList<>(noteList.size());

    for (Note note : noteList) {
      NoteDto noteDto = noteMapper.noteToNoteDto(note);
      noteDtoList.add(noteDto);
    }

    return new PageImpl<>(noteDtoList, pageable, result.total().hitCount());
  }

  private SearchPredicate formSearchPredicates(String query, SearchScope<Note> searchScope) {
    SearchPredicateFactory factory = searchScope.predicate();

    BooleanPredicateClausesStep<?> booleanJunction = factory.bool();
    prepareNamePredicate(booleanJunction, factory, query);
    SearchPredicate boolPredicate = booleanJunction.toPredicate();

    return boolPredicate;
  }

  private void prepareNamePredicate(BooleanPredicateClausesStep<?> booleanJunction,
      SearchPredicateFactory factory, String query) {
    if (org.apache.commons.lang3.StringUtils.isBlank(query)) {
      return;
    }
    booleanJunction.must(factory.match().field("noteName").matching(query).fuzzy(2).toPredicate());
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
