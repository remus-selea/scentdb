package com.github.remusselea.scentdb.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.dto.request.PerfumerModel;
import com.github.remusselea.scentdb.dto.view.View.PerfumerView;
import com.github.remusselea.scentdb.service.PerfumerService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
public class PerfumerController {

  private PerfumerService perfumerService;

  public PerfumerController(PerfumerService perfumerService) {
    this.perfumerService = perfumerService;
  }

  @GetMapping("/perfumers/{perfumerId}")
  public PerfumerDto getPerfumerById(@PathVariable Long perfumerId) {
    log.info("Getting perfumer by Id: {}", perfumerId);

    return perfumerService.getPerfumerById(perfumerId);
  }

  /**
   * Get all perfumers.
   *
   * @return all perfumers.
   */
  @GetMapping("/perfumers")
  @JsonView(value = PerfumerView.class)
  public List<PerfumerDto> getAllPerfumes() {
    log.info("Getting all perfumers");

    return perfumerService.getAllPerfumers();
  }

  /**
   * Saves a perfumer.
   *
   * @param perfumer the perfumer request body.
   * @return the saved perfumer.
   */
  @JsonView(value = PerfumerView.class)
  @PostMapping("/perfumers")
  public PerfumerDto savePerfumer(@RequestParam("image")  MultipartFile imageFile,
      @RequestParam("perfumer") String perfumer) {
    log.info("Saving a perfumer");
    PerfumerModel perfumerModel = deserializeStringToPerfumerModel(perfumer);

    return perfumerService.savePerfumer(perfumerModel, imageFile);
  }

  @DeleteMapping("/perfumers/{perfumerId}")
  public void removePerfume(@PathVariable Long perfumerId) {

    perfumerService.removePerfumerById(perfumerId);
  }

  @JsonView(value = PerfumerView.class)
  @GetMapping("/perfumers/search")
  public Page searchPerfumers(@RequestParam(value = "q", required = false) String query,
      @PageableDefault(size = 9) Pageable pageable) {
    log.info("searching perfumers");

    return perfumerService.search(pageable, query);
  }



  private PerfumerModel deserializeStringToPerfumerModel(String perfumer) {
    PerfumerModel perfumerModel = null;
    try {
      perfumerModel = new ObjectMapper().readValue(perfumer, PerfumerModel.class);
    } catch (JsonProcessingException e) {
      log.error("Could not parse perfumer string, cause {0}", e);
    }
    return perfumerModel;
  }


}
