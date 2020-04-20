package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.model.PerfumeRequest;
import com.github.remusselea.scentdb.model.response.PerfumeResponse;
import com.github.remusselea.scentdb.service.PerfumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scentdb/v1")
@Slf4j
public class PerfumeController {

  private PerfumeService perfumeService;

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
   * @param perfumeRequest the perfume request body.
   * @return the saved perfume.
   */
  @PostMapping("/perfumes")
  public PerfumeResponse savePerfume(@RequestBody PerfumeRequest perfumeRequest) {

    return perfumeService.savePerfume(perfumeRequest);
  }


  /**
   * Updates a perfume.
   *
   * @param perfumeRequest the request body with the perfume data
   * @param perfumeId      the id of the perfume
   * @return the updated perfume.
   */
  @PutMapping("/perfumes/{perfumeId}")
  public PerfumeResponse updatePerfume(@RequestBody PerfumeRequest perfumeRequest,
                                       @PathVariable Long perfumeId) {

    return perfumeService.savePerfume(perfumeRequest);
  }


  @DeleteMapping("/perfumes/{perfumeId}")
  public void removePerfume(@PathVariable Long perfumeId) {

    perfumeService.removePerfumeById(perfumeId);
  }


}
