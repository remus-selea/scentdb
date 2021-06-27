package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.dto.mapper.CompanyMapper;
import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.dto.request.CompanyModel;
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
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Transactional
@Service
public class CompanyService {

  private PerfumerRepository perfumerRepository;

  private CompanyRepository companyRepository;

  private PerfumeRepository perfumeRepository;

  private CompanyMapper companyMapper;

  private ImageService imageService;

  @Value("${companies.images.dir:${user.home}}")
  public String uploadDir;

  public CompanyService(PerfumerRepository perfumerRepository,
      CompanyRepository companyRepository,
      PerfumeRepository perfumeRepository,
      CompanyMapper companyMapper, ImageService imageService) {
    this.perfumerRepository = perfumerRepository;
    this.companyRepository = companyRepository;
    this.perfumeRepository = perfumeRepository;
    this.companyMapper = companyMapper;
    this.imageService = imageService;
  }

  public CompanyDto getCompanyById(long companyId) {
    Company company = companyRepository.findById(companyId).orElseThrow(
        () -> new EntityNotFoundException("Could not find company with id: " + companyId));

    return companyMapper.companyToCompanyDto(company);
  }

  public CompanyDto saveCompany(CompanyModel companyModel, MultipartFile multipartImageFile) {
    Company companyToSave = companyMapper.companyModelToCompany(companyModel);

    Set<Perfume> perfumeList = new HashSet<>(companyModel.getPerfumeIdList().size());
    for (Long perfumeId : companyModel.getPerfumeIdList()) {
      Optional<Perfume> optionalPerfume = perfumeRepository.findById(perfumeId);
      optionalPerfume.ifPresent(perfumeList::add);
    }
    companyToSave.setPerfumes(perfumeList);


    Set<Perfumer> perfumerList = new HashSet<>(companyModel.getPerfumerIdList().size());
    for (Long perfumerId : companyModel.getPerfumerIdList()) {
      Optional<Perfumer> optionalPerfumer = perfumerRepository.findById(perfumerId);
      optionalPerfumer.ifPresent(perfumerList::add);
    }
    companyToSave.setPerfumers(perfumerList);

    String imageFileName = imageService.storeImage(multipartImageFile, uploadDir);
    String companyImagePath = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/scentdb/v1/images/companies/")
        .path(imageFileName)
        .toUriString();
    companyToSave.setImagePath(companyImagePath);

    Company savedCompany = companyRepository.save(companyToSave);

    return companyMapper.companyToCompanyDto(savedCompany);
  }

  public List<CompanyDto> getAllCompanies() {
    List<Company> companyList = companyRepository.findAll();
    List<CompanyDto> companyDtoList = new ArrayList<>(companyList.size());

    for (Company company : companyList) {
      CompanyDto companyDto = companyMapper.companyToCompanyDto(company);
      companyDtoList.add(companyDto);
    }

    return companyDtoList;
  }

  public void removeCompanyById(Long companyId) {
    companyRepository.deleteById(companyId);
  }

}
