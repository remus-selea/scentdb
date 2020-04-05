package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.model.response.PerfumeResponse;
import com.github.remusselea.scentdb.service.PerfumesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
   * @param id of the perfume,
   * @return a perfume.
   */
  @GetMapping("/perfume/{id}")
  public PerfumeResponse getPerfumeById(@PathVariable Long id) {
    log.info("Getting perfume by Id: {}", id);
    PerfumeResponse perfumeResponse = perfumesService.getPerfumeById(id);

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

}
