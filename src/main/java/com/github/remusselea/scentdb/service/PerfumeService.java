package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.dto.mapper.NoteMapper;
import com.github.remusselea.scentdb.dto.mapper.PerfumeImageMapper;
import com.github.remusselea.scentdb.dto.mapper.PerfumeMapper;
import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeImageDto;
import com.github.remusselea.scentdb.dto.request.PerfumeRequest;
import com.github.remusselea.scentdb.dto.response.PerfumeResponse;
import com.github.remusselea.scentdb.exception.FileStorageException;
import com.github.remusselea.scentdb.model.entity.Note;
import com.github.remusselea.scentdb.model.entity.Perfume;
import com.github.remusselea.scentdb.model.entity.PerfumeImage;
import com.github.remusselea.scentdb.model.entity.PerfumeNote;
import com.github.remusselea.scentdb.repo.PerfumeRepository;
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
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Transactional
@Service
public class PerfumeService {

  private PerfumeRepository perfumeRepository;

  private PerfumeMapper perfumeMapper;

  private NoteMapper noteMapper;

  private PerfumeImageMapper perfumeImageMapper;

  @Value("${perfumes.images.dir:${user.home}}")
  public String uploadDir;

  /**
   * All args constructor.
   *
   * @param perfumeRepository the perfume repository.
   * @param perfumeMapper     mapper that maps perfumes to dto.
   * @param noteMapper        mapper that maps notes to dto.
   */
  public PerfumeService(PerfumeRepository perfumeRepository,
      PerfumeMapper perfumeMapper, NoteMapper noteMapper,
      PerfumeImageMapper perfumeImageMapper) {
    this.perfumeRepository = perfumeRepository;
    this.perfumeMapper = perfumeMapper;
    this.noteMapper = noteMapper;
    this.perfumeImageMapper = perfumeImageMapper;
  }

  /**
   * Gets all existing {@link Perfume} from the {@link PerfumeRepository} and returns them in the
   * form of a {@link PerfumeResponse}.
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
   * Gets a {@link Perfume} by Id from the {@link PerfumeRepository} and returns it in the form of a
   * {@link PerfumeResponse}.
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
  public PerfumeResponse savePerfume(PerfumeRequest perfumeRequest, MultipartFile[] imageFiles) {
    Perfume perfumeToSave = perfumeMapper.perfumeRequestToPerfume(perfumeRequest);

    for (MultipartFile multipartImageFile : imageFiles) {
      String imageFileName = storeImage(multipartImageFile);

      String perfumeImagePath = ServletUriComponentsBuilder.fromCurrentContextPath()
          .path("/scentdb/v1/images/perfumes/")
          .path(imageFileName)
          .toUriString();

      PerfumeImage perfumeImage = new PerfumeImage();
      perfumeImage.setImagePath(perfumeImagePath);
      perfumeToSave.addPerfumeImage(perfumeImage);
    }

    Perfume savedPerfume = perfumeRepository.save(perfumeToSave);

    PerfumeResponse savedPerfumeResponse = createPerfumeResponse();
    addInfoToPerfumeResponse(savedPerfumeResponse, savedPerfume);

    return savedPerfumeResponse;
  }

  /**
   * Creates a {@link PerfumeResponse} and adds an empty list of {@link PerfumeDto} and empty map of
   * {@link Map noteDtoMap} to it.
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

    List<PerfumeImageDto> perfumeImageDtoList = new ArrayList<>(perfume.getPerfumeImages().size());
    for (PerfumeImage perfumeImage : perfume.getPerfumeImages()) {
      PerfumeImageDto perfumeImageDto = perfumeImageMapper
          .perfumeImageToPerfumeImageDto(perfumeImage);
      perfumeImageDtoList.add(perfumeImageDto);
    }
    perfumeDto.setPerfumeImageDtoList(perfumeImageDtoList);

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
