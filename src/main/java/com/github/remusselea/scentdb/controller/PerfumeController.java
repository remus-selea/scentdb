package com.github.remusselea.scentdb.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.remusselea.scentdb.dto.request.PerfumeModel;
import com.github.remusselea.scentdb.dto.request.PerfumeRequest;
import com.github.remusselea.scentdb.dto.response.PerfumeResponse;
import com.github.remusselea.scentdb.dto.view.View.PerfumeView;
import com.github.remusselea.scentdb.service.PerfumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/scentdb/v1")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class PerfumeController {

  private final PerfumeService perfumeService;

  public PerfumeController(PerfumeService perfumeService) {
    this.perfumeService = perfumeService;
  }

  /**
   * Get a perfume by id.
   *
   * @param perfumeId the id of the perfume,
   * @return a perfume.
   */
  @GetMapping("/perfumes/{perfumeId}")
  public PerfumeResponse getPerfumeById(@PathVariable Long perfumeId) {
    log.info("Getting perfume by Id: {}", perfumeId);

    return perfumeService.getPerfumeById(perfumeId);
  }

  /**
   * Get all perfumes.
   *
   * @return all perfumes.
   */
  @GetMapping("/perfumes")
  public PerfumeResponse getAllPerfumes() {
    log.info("Getting all perfumes");

    return perfumeService.getAllPerfumes();
  }

  /**
   * Saves a perfume.
   *
   * @param perfume the perfume request body.
   * @return the saved perfume.
   */
  @PostMapping("/perfumes")
  @JsonView(value = PerfumeView.class)
  public PerfumeResponse savePerfume(@RequestParam("image")  MultipartFile[] imageFiles,
      @RequestParam("perfume") String perfume) {
    log.info("Saving a perfume");
    PerfumeModel perfumeModel = deserializeStringToPerfumeModel(perfume);

    return perfumeService.savePerfume(perfumeModel, imageFiles);
  }


  /**
   * Updates a perfume.
   *
   * @param perfume the request body with the perfume data
   * @param perfumeId the id of the perfume
   * @return the updated perfume.
   */
  @PutMapping("/perfumes/{perfumeId}")
  @JsonView(value = PerfumeView.class)
  public PerfumeResponse updatePerfume(@RequestParam("image") MultipartFile[] imageFiles,
      @RequestParam("perfume") String perfume, @PathVariable Long perfumeId) {
    PerfumeModel perfumeModel = deserializeStringToPerfumeModel(perfume);

    return perfumeService.savePerfume(perfumeModel, imageFiles);
  }


  @DeleteMapping("/perfumes/{perfumeId}")
  public void removePerfume(@PathVariable Long perfumeId) {

    perfumeService.removePerfumeById(perfumeId);
  }

  private PerfumeRequest deserializeStringToPerfumeRequest(String perfume) {
    PerfumeRequest perfumeRequest = null;
    try {
      perfumeRequest = new ObjectMapper().readValue(perfume, PerfumeRequest.class);
    } catch (JsonProcessingException e) {
      log.error("Could not deserialize the string to a PerfumeRequest, cause {0}", e);
    }
    return perfumeRequest;
  }

  private PerfumeModel deserializeStringToPerfumeModel(String perfume) {
    PerfumeModel perfumeModel = null;
    try {
      perfumeModel = new ObjectMapper().readValue(perfume, PerfumeModel.class);
    } catch (JsonProcessingException e) {
      log.error("Could not deserialize the string to a PerfumeModel, cause {0}", e);
    }
    return perfumeModel;
  }

}
