package com.github.remusselea.scentdb.service;


import com.github.remusselea.scentdb.dto.mapper.PerfumerMapper;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.dto.request.PerfumerModel;
import com.github.remusselea.scentdb.model.entity.Company;
import com.github.remusselea.scentdb.model.entity.Perfume;
import com.github.remusselea.scentdb.model.entity.Perfumer;
import com.github.remusselea.scentdb.repo.CompanyRepository;
import com.github.remusselea.scentdb.repo.PerfumeRepository;
import com.github.remusselea.scentdb.repo.PerfumerRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Transactional
@Service
public class PerfumerService {

  private PerfumerRepository perfumerRepository;

  private CompanyRepository companyRepository;

  private PerfumeRepository perfumeRepository;

  private PerfumerMapper perfumerMapper;

  private ImageService imageService;

  @Value("${perfumers.images.dir:${user.home}}")
  public String uploadDir;

  public PerfumerService(PerfumerRepository perfumerRepository,
      CompanyRepository companyRepository,
      PerfumeRepository perfumeRepository,
      PerfumerMapper perfumerMapper, ImageService imageService) {
    this.perfumerRepository = perfumerRepository;
    this.companyRepository = companyRepository;
    this.perfumeRepository = perfumeRepository;
    this.perfumerMapper = perfumerMapper;
    this.imageService = imageService;
  }

  public PerfumerDto getPerfumerById(long perfumerId) {
    Optional<Perfumer> perfumer = perfumerRepository.findById(perfumerId);

    return perfumerMapper.perfumerToPerfumerDto(perfumer.get());
  }

  public PerfumerDto savePerfumer(PerfumerModel perfumerModel, MultipartFile multipartImageFile) {
    Perfumer perfumerToSave = perfumerMapper.perfumerModelToPerfumer(perfumerModel);

    Optional<Company> optionalCompany = companyRepository.findById(perfumerModel.getCompanyId());
    optionalCompany.ifPresent(perfumerToSave::setCompany);

    Set<Perfume> perfumeList = new HashSet<>(perfumerModel.getPerfumeIdList().size());
    for (Long perfumeId : perfumerModel.getPerfumeIdList()) {
      Optional<Perfume> optionalPerfume = perfumeRepository.findById(perfumeId);
      optionalPerfume.ifPresent(perfumeList::add);
    }
    perfumerToSave.setPerfumes(perfumeList);

    String imageFileName = imageService.storeImage(multipartImageFile, uploadDir);
    String perfumerImagePath = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/scentdb/v1/images/perfumers/")
        .path(imageFileName)
        .toUriString();
    perfumerToSave.setImagePath(perfumerImagePath);

    Perfumer savedPerfumer = perfumerRepository.save(perfumerToSave);

    PerfumerDto perfumerDto = perfumerMapper.perfumerToPerfumerDto(savedPerfumer);
    return perfumerDto;
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
