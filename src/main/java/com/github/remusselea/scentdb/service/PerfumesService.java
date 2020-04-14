package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.data.Note;
import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.data.PerfumeNote;
import com.github.remusselea.scentdb.data.repo.PerfumeNoteRepository;
import com.github.remusselea.scentdb.data.repo.PerfumeRepository;
import com.github.remusselea.scentdb.mapping.NoteMapper;
import com.github.remusselea.scentdb.mapping.PerfumeMapper;
import com.github.remusselea.scentdb.model.response.PerfumeResponse;
import com.github.remusselea.scentdb.model.response.note.NoteDto;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PerfumesService {

  private PerfumeRepository perfumeRepository;

  private PerfumeNoteRepository perfumeNoteRepository;

  private PerfumeMapper perfumeMapper;

  private NoteMapper noteMapper;

  /**
   * All args constructor.
   *
   * @param perfumeRepository the perfume CrudRepository.
   * @param perfumeMapper     mapper that maps perfumes to dto.
   * @param noteMapper        mapper that maps notes to dto.
   */
  public PerfumesService(PerfumeRepository perfumeRepository, PerfumeNoteRepository perfumeNoteRepository, PerfumeMapper perfumeMapper, NoteMapper noteMapper) {
    this.perfumeRepository = perfumeRepository;
    this.perfumeNoteRepository = perfumeNoteRepository;
    this.perfumeMapper = perfumeMapper;
    this.noteMapper = noteMapper;
  }




  public PerfumeResponse getAllPerfumes() {
    Iterable<Perfume> perfumes = perfumeRepository.findAll();

    PerfumeResponse perfumeResponse = createPerfumeResponse();

    perfumes.forEach(perfume -> {
      addInfoToPerfumeResponse(perfumeResponse, perfume);

    });

    return perfumeResponse;
  }

  @Transactional
  public PerfumeResponse getPerfumeById(Long id) {
    Optional<Perfume> optionalPerfume = perfumeRepository.findById(id);

    PerfumeResponse perfumeResponse = createPerfumeResponse();

    if (optionalPerfume.isPresent()) {
      Perfume perfume = optionalPerfume.get();
      addInfoToPerfumeResponse(perfumeResponse, perfume);
    }

    return perfumeResponse;
  }

  public PerfumeResponse savePerfume(PerfumeResponse perfumeResponse) {
    Perfume perfumeToSave = createPerfumeFromPerfumeResponse(perfumeResponse);

    return savePerfume2(perfumeToSave);
  }

  @Transactional
  public PerfumeResponse savePerfume2(Perfume perfumeToSave) {

    Perfume savedPerfume = perfumeRepository.save(perfumeToSave);

    PerfumeResponse savedPerfumeResponse = createPerfumeResponse();
    addInfoToPerfumeResponse(savedPerfumeResponse, savedPerfume);

    return savedPerfumeResponse;
  }


  private Perfume createPerfumeFromPerfumeResponse(PerfumeResponse perfumeResponse) {
    Perfume perfume = perfumeMapper.perfumeDtoToPerfume(perfumeResponse.getPerfumeDtoList().get(0));
    Set<PerfumeNote> perfumeNoteSet = perfume.getPerfumeNotes();
    Map<Long, NoteDto> noteDtoMap = perfumeResponse.getNoteDtoMap();

    for (PerfumeNote perfumeNote : perfumeNoteSet) {
      Note note = perfumeNote.getNote();
      long noteId = note.getNoteId();
      NoteDto noteDto = noteDtoMap.get(noteId);

      note.setNoteName(noteDto.getNoteName());
      note.setImgPath(noteDto.getImgPath());
    }

    return perfume;
  }

  private PerfumeResponse createPerfumeResponse() {
    PerfumeResponse perfumeResponse = new PerfumeResponse();

    List<PerfumeDto> perfumeDtoList = new ArrayList<>();
    // Set empty perfumeWrapperList to perfume response
    perfumeResponse.setPerfumeDtoList(perfumeDtoList);

    Map<Long, NoteDto> noteDtoMap = new HashMap<>();
    // Set empty noteDtoMap to perfume response
    perfumeResponse.setNoteDtoMap(noteDtoMap);
    return perfumeResponse;
  }

  private void addInfoToPerfumeResponse(PerfumeResponse perfumeResponse, Perfume perfume) {
    PerfumeDto perfumeDto = perfumeMapper.perfumeToPerfumeDto(perfume);
    perfumeResponse.getPerfumeDtoList().add(perfumeDto);

    for (PerfumeNote perfumeNote : perfume.getPerfumeNotes()) {
       Note note = perfumeNote.getNote();
      NoteDto noteDto = noteMapper.perfumeNoteToNoteDto(perfumeNote);
      perfumeResponse.getNoteDtoMap().put(note.getNoteId(), noteDto);
    }
  }

}
