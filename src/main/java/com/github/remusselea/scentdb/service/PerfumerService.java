package com.github.remusselea.scentdb.service;


import com.github.remusselea.scentdb.dto.mapper.PerfumerMapper;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.model.entity.Perfumer;
import com.github.remusselea.scentdb.repo.PerfumerRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Transactional
@Service
public class PerfumerService {

  private PerfumerRepository perfumerRepository;

  private PerfumerMapper perfumerMapper;

  private ImageService imageService;

  @Value("${perfumers.images.dir:${user.home}}")
  public String uploadDir;

  public PerfumerService(PerfumerRepository perfumerRepository,
      PerfumerMapper perfumerMapper, ImageService imageService) {
    this.perfumerRepository = perfumerRepository;
    this.perfumerMapper = perfumerMapper;
    this.imageService = imageService;
  }

  public PerfumerDto getPerfumerById(long perfumerId) {
    Perfumer perfumer = perfumerRepository.getOne(perfumerId);

    return perfumerMapper.perfumerToPerfumerDto(perfumer);
  }

  public PerfumerDto savePerfumer(PerfumerDto perfumerDto, MultipartFile multipartImageFile) {
    Perfumer perfumerToSave = perfumerMapper.perfumerDtoToPerfumer(perfumerDto);

    String imageFileName = imageService.storeImage(multipartImageFile, uploadDir);
    String perfumerImagePath = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/scentdb/v1/images/perfumers/")
        .path(imageFileName)
        .toUriString();

    perfumerToSave.setImagePath(perfumerImagePath);

    Perfumer savedPerfumer = perfumerRepository.save(perfumerToSave);

    return perfumerMapper.perfumerToPerfumerDto(savedPerfumer);
  }

  public List<PerfumerDto> getAllPerfumers() {
    List<Perfumer> perfumerList = perfumerRepository.findAll();
    List<PerfumerDto> perfumerDtoList = new ArrayList<>(perfumerList.size());

    for (Perfumer perfumer : perfumerList) {
      PerfumerDto perfumerDto = perfumerMapper.perfumerToPerfumerDto(perfumer);
      perfumerDtoList.add(perfumerDto);
    }

    return perfumerDtoList;
  }


  public void removePerfumerById(Long perfumerId) {
    perfumerRepository.deleteById(perfumerId);
  }

}
