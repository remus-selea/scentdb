package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.model.response.PerfumeResponse;
import com.github.remusselea.scentdb.service.PerfumesService;
import lombok.extern.slf4j.Slf4j;
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

  private PerfumesService perfumesService;

  public PerfumeController(PerfumesService perfumesService) {
    this.perfumesService = perfumesService;
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
    PerfumeResponse perfumeResponse = perfumesService.getPerfumeById(perfumeId);

    return perfumeResponse;
  }

  /**
   * Get all perfumes.
   *
   * @return all perfumes.
   */
  @GetMapping("/perfumes")
  public PerfumeResponse getAllPerfumes() {
    log.info("Getting all perfumes");
    PerfumeResponse perfumeResponse = perfumesService.getAllPerfumes();

    return perfumeResponse;
  }

  /**
   * Saves a perfume.
   *
   * @param perfumeResponse the perfume request body.
   * @return the saved perfume.
   */
  @PostMapping("/perfumes")
  public PerfumeResponse savePerfume(@RequestBody PerfumeResponse perfumeResponse) {

    PerfumeResponse savedPerfumeResponse = perfumesService.savePerfume(perfumeResponse);

    return savedPerfumeResponse;
  }


  /**
   * Updates a perfume.
   *
   * @param perfumeResponse the request body with the perfume data
   * @param perfumeId the id of the perfume
   * @return the updated perfume.
   */
  @PutMapping("/perfumes/{perfumeId}")
  public PerfumeResponse updatePerfume(@RequestBody PerfumeResponse perfumeResponse, @PathVariable Long perfumeId) {

    PerfumeResponse savedPerfumeResponse = perfumesService.savePerfume(perfumeResponse);

    return savedPerfumeResponse;
  }


}
