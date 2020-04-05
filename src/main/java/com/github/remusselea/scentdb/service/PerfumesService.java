package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.data.Note;
import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.data.PerfumeNote;
import com.github.remusselea.scentdb.data.repo.PerfumeRepository;
import com.github.remusselea.scentdb.mapping.NoteMapper;
import com.github.remusselea.scentdb.mapping.PerfumeMapper;
import com.github.remusselea.scentdb.model.response.PerfumeResponse;
import com.github.remusselea.scentdb.model.response.note.NoteDto;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PerfumesService {

  private PerfumeRepository perfumeRepository;

  private PerfumeMapper perfumeMapper;

  private NoteMapper noteMapper;

  /**
   * All args constructor.
   *
   * @param perfumeRepository the perfume CrudRepository.
   * @param perfumeMapper     mapper that maps perfumes to dto.
   * @param noteMapper        mapper that maps notes to dto.
   */

  public PerfumesService(PerfumeRepository perfumeRepository,
                         PerfumeMapper perfumeMapper, NoteMapper noteMapper) {
    this.perfumeRepository = perfumeRepository;
    this.perfumeMapper = perfumeMapper;
    this.noteMapper = noteMapper;
  }

  @Transactional
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
    Optional<Perfume> perfumes = perfumeRepository.findById(id);

    PerfumeResponse perfumeResponse = createPerfumeResponse();

    if (perfumes.isPresent()) {
      Perfume perfume = perfumes.get();
      addInfoToPerfumeResponse(perfumeResponse, perfume);
    }

    return perfumeResponse;
  }


  private PerfumeResponse createPerfumeResponse() {
    PerfumeResponse perfumeResponse = new PerfumeResponse();

    List<PerfumeWrapper> perfumeWrapperList = new ArrayList<>();
    // Set empty perfumeWrapperList to perfume response
    perfumeResponse.setPerfumeWrapperList(perfumeWrapperList);

    Map<Long, NoteDto> noteDtoMap = new HashMap<>();
    // Set empty noteDtoMap to perfume response
    perfumeResponse.setNoteDtoMap(noteDtoMap);
    return perfumeResponse;
  }

  private void addInfoToPerfumeResponse(PerfumeResponse perfumeResponse, Perfume perfume) {
    PerfumeWrapper perfumeWrapper = perfumeMapper.perfumeToPerfumeWrapper(perfume);
    perfumeResponse.getPerfumeWrapperList().add(perfumeWrapper);

    for (PerfumeNote perfumeNote : perfume.getPerfumeNotes()) {
      Note note = perfumeNote.getNote();
      NoteDto noteDto = noteMapper.perfumeNoteToNoteDto(perfumeNote);
      perfumeResponse.getNoteDtoMap().put(note.getNoteId(), noteDto);
    }
  }

}
