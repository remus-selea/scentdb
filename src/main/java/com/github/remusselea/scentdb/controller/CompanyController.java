package com.github.remusselea.scentdb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.service.CompanyService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "http://localhost:3000")
public class CompanyController {

  private CompanyService companyService;

  public CompanyController(CompanyService companyService) {
    this.companyService = companyService;
  }

  @GetMapping("/companies/{companyId}")
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
  public CompanyDto savePerfumer(@RequestParam("image") MultipartFile imageFile,
      @RequestParam("company") String company) {
    log.info("Saving a company");
    CompanyDto companyDto = deserializeStringToCompanyDto(company);

    return companyService.saveCompany(companyDto, imageFile);
  }

  /**
   * Get all companies.
   *
   * @return all companies.
   */
  @GetMapping("/companies")
  public List<CompanyDto> getAllCompanies() {
    log.info("Getting all companies");

    return companyService.getAllCompanies();
  }


  @DeleteMapping("/companies/{companyId}")
  public void removePerfume(@PathVariable Long companyId) {

    companyService.removeCompanyById(companyId);
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


}
