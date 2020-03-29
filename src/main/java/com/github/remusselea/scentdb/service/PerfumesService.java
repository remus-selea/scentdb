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


  public PerfumesService(PerfumeRepository perfumeRepository,
                         PerfumeMapper perfumeMapper, NoteMapper noteMapper) {
    this.perfumeRepository = perfumeRepository;
    this.perfumeMapper = perfumeMapper;
    this.noteMapper = noteMapper;
  }


  @Transactional
  public PerfumeResponse getAllPerfumes() {
    Iterable<Perfume> perfumes = perfumeRepository.findAll();
    PerfumeResponse perfumeResponse = new PerfumeResponse();

    List<PerfumeWrapper> perfumeWrapperList = new ArrayList<>();
    perfumeResponse.setPerfumeWrapperList(perfumeWrapperList);

    perfumes.forEach(perfume -> {
      createPerfumeResponse(perfumeResponse, perfume);

    });

    return perfumeResponse;
  }

  private void createPerfumeResponse(PerfumeResponse perfumeResponse, Perfume perfume) {
    PerfumeWrapper perfumeWrapper = perfumeMapper.perfumeToPerfumeWrapper(perfume);

    Map<Long, NoteDto> noteDtoMap = new HashMap<>();
    for (PerfumeNote perfumeNote : perfume.getPerfumeNotes()) {
      Note note = perfumeNote.getNote();
      NoteDto noteDto = noteMapper.perfumeNoteToNoteDto(perfumeNote);

      noteDtoMap.put(note.getNoteId(), noteDto);
    }

    perfumeResponse.setNoteDtoMap(noteDtoMap);
    perfumeResponse.getPerfumeWrapperList().add(perfumeWrapper);
  }


  @Transactional
  public PerfumeResponse getPerfumeById(Long id) {
    Optional<Perfume> perfumes = perfumeRepository.findById(id);
    PerfumeResponse perfumeResponse = new PerfumeResponse();

    List<PerfumeWrapper> perfumeWrapperList = new ArrayList<>();
    perfumeResponse.setPerfumeWrapperList(perfumeWrapperList);

    if (perfumes.isPresent()) {
      Perfume perfume = perfumes.get();
      createPerfumeResponse(perfumeResponse, perfume);
    }

    return perfumeResponse;
  }
}
