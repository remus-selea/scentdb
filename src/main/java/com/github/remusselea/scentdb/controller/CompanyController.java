package com.github.remusselea.scentdb.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.dto.request.CompanyModel;
import com.github.remusselea.scentdb.dto.view.View.CompanyView;
import com.github.remusselea.scentdb.service.CompanyService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/scentdb/v1")
@Slf4j
public class CompanyController {

  private CompanyService companyService;

  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @GetMapping("/companies/{companyId}")
  @JsonView(value = CompanyView.class)
  public CompanyDto getCompanyById(@PathVariable Long companyId) {
    log.info("Getting company by id: {}", companyId);

    return companyService.getCompanyById(companyId);
  }


  /**
   * Saves a company.
   *
   * @param company the company request body.
   * @return the saved company.
   */
  @PostMapping("/companies")
  @JsonView(value = CompanyView.class)
  public CompanyDto saveCompany(@RequestParam("image") MultipartFile imageFile,
      @RequestParam("company") String company) {
    log.info("Saving a company");
    CompanyModel companyModel = deserializeStringToCompanyModel(company);

    return companyService.saveCompany(companyModel, imageFile);
  }

  /**
   * Get all companies.
   *
   * @return all companies.
   */
  @GetMapping("/companies")
  @JsonView(value = CompanyView.class)
  public List<CompanyDto> getAllCompanies() {
    log.info("Getting all companies");

    return companyService.getAllCompanies();
  }


  @DeleteMapping("/companies/{companyId}")
  public void removePerfume(@PathVariable Long companyId) {

    companyService.removeCompanyById(companyId);
  }

  @GetMapping("/companies/search")
  @JsonView(value = CompanyView.class)
  public Page searchCompanies(@RequestParam(value = "q", required = false) String query,
      @PageableDefault(size = 9) Pageable pageable) {
    log.info("searching companies");
    return companyService.search(pageable, query);
  }


  private CompanyDto deserializeStringToCompanyDto(String company) {
    CompanyDto companyDto = null;
    try {
      companyDto = new ObjectMapper().readValue(company, CompanyDto.class);
    } catch (JsonProcessingException e) {
      log.error("Could not parse perfumer string, cause {0}", e);
    }
    return companyDto;
  }


  private CompanyModel deserializeStringToCompanyModel(String company) {
    CompanyModel companyModel = null;
    try {
      companyModel = new ObjectMapper().readValue(company, CompanyModel.class);
    } catch (JsonProcessingException e) {
      log.error("Could not parse perfumer string, cause {0}", e);
    }
    return companyModel;
  }

}
