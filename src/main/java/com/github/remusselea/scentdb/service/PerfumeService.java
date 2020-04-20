package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.data.Note;
import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.data.PerfumeNote;
import com.github.remusselea.scentdb.data.repo.PerfumeRepository;
import com.github.remusselea.scentdb.mapping.NoteMapper;
import com.github.remusselea.scentdb.mapping.PerfumeMapper;
import com.github.remusselea.scentdb.model.PerfumeRequest;
import com.github.remusselea.scentdb.model.note.NoteDto;
import com.github.remusselea.scentdb.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.model.response.PerfumeResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class PerfumeService {

  private PerfumeRepository perfumeRepository;

  private PerfumeMapper perfumeMapper;

  private NoteMapper noteMapper;

  /**
   * All args constructor.
   *
   * @param perfumeRepository the perfume repository.
   * @param perfumeMapper     mapper that maps perfumes to dto.
   * @param noteMapper        mapper that maps notes to dto.
   */
  public PerfumeService(PerfumeRepository perfumeRepository,
                        PerfumeMapper perfumeMapper, NoteMapper noteMapper) {
    this.perfumeRepository = perfumeRepository;
    this.perfumeMapper = perfumeMapper;
    this.noteMapper = noteMapper;
  }

  /**
   * Gets all existing {@link Perfume} from the {@link PerfumeRepository}
   * and returns them in the form of a {@link PerfumeResponse}.
   *
   * @return a {@link PerfumeResponse} containing all the perfumes and their notes.
   */
  public PerfumeResponse getAllPerfumes() {
    Iterable<Perfume> perfumes = perfumeRepository.findAll();

    PerfumeResponse perfumeResponse = createPerfumeResponse();
    perfumes.forEach(perfume -> addInfoToPerfumeResponse(perfumeResponse, perfume));

    return perfumeResponse;
  }

  /**
   * Gets a {@link Perfume} by Id from the {@link PerfumeRepository}
   * and returns it in the form of a {@link PerfumeResponse}.
   *
   * @param id the identifier of the {@link Perfume}.
   * @return a {@link PerfumeResponse} containing a perfume and it's notes.
   */
  public PerfumeResponse getPerfumeById(Long id) {
    Optional<Perfume> optionalPerfume = perfumeRepository.findById(id);

    PerfumeResponse perfumeResponse = createPerfumeResponse();
    if (optionalPerfume.isPresent()) {
      Perfume perfume = optionalPerfume.get();
      addInfoToPerfumeResponse(perfumeResponse, perfume);
    }

    return perfumeResponse;
  }

  /**
   * Creates or updates a {@link Perfume} in the configured {@link PerfumeRepository}.
   *
   * @param perfumeRequest the object containing the data of a perfume to be created or updated.
   * @return returns a {@link PerfumeResponse} containing the perfume saved in the database.
   */
  public PerfumeResponse savePerfume(PerfumeRequest perfumeRequest) {
    Perfume perfumeToSave = perfumeMapper.perfumeRequestToPerfume(perfumeRequest);
    Perfume savedPerfume = perfumeRepository.save(perfumeToSave);

    PerfumeResponse savedPerfumeResponse = createPerfumeResponse();
    addInfoToPerfumeResponse(savedPerfumeResponse, savedPerfume);

    return savedPerfumeResponse;
  }

  /**
   * Creates a {@link PerfumeResponse}
   * and adds an empty list of {@link PerfumeDto} and empty map of {@link Map noteDtoMap} to it.
   *
   * @return the created PerfumeResponse.
   */
  private PerfumeResponse createPerfumeResponse() {
    PerfumeResponse perfumeResponse = new PerfumeResponse();
    // Create and set the empty perfumeDtoList to perfume response
    List<PerfumeDto> perfumeDtoList = new ArrayList<>();
    perfumeResponse.setPerfumeDtoList(perfumeDtoList);
    // Create and set the empty noteDtoMap to perfume response
    Map<Long, NoteDto> noteDtoMap = new HashMap<>();
    perfumeResponse.setNoteDtoMap(noteDtoMap);

    return perfumeResponse;
  }

  /**
   * Adds the data of a {@link Perfume} to a {@link PerfumeResponse}.
   */
  private void addInfoToPerfumeResponse(PerfumeResponse perfumeResponse, Perfume perfume) {
    // map perfume to perfumeDto
    PerfumeDto perfumeDto = perfumeMapper.perfumeToPerfumeDto(perfume);
    // add each mapped perfumeDto into the perfumeDtoList of perfumeResponse
    perfumeResponse.getPerfumeDtoList().add(perfumeDto);

    // map each PerfumeNote to a NoteDto and put it in the noteDtoMap
    for (PerfumeNote perfumeNote : perfume.getPerfumeNotes()) {
      NoteDto noteDto = noteMapper.perfumeNoteToNoteDto(perfumeNote);
      Note note = perfumeNote.getNote();
      perfumeResponse.getNoteDtoMap().put(note.getNoteId(), noteDto);
    }
  }

  public void removePerfumeById(Long perfumeId) {
    perfumeRepository.deleteById(perfumeId);
  }

}
