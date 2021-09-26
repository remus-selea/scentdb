package com.github.remusselea.scentdb.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.remusselea.scentdb.dto.Filter;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.request.PerfumeModel;
import com.github.remusselea.scentdb.dto.request.PerfumeRequest;
import com.github.remusselea.scentdb.dto.response.PerfumeResponse;
import com.github.remusselea.scentdb.dto.view.View.PerfumeView;
import com.github.remusselea.scentdb.service.PerfumeService;
import com.github.remusselea.scentdb.service.PerfumeSearchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
public class PerfumeController {

  private final PerfumeService perfumeService;

  private final PerfumeSearchService perfumeSearchService;

  public PerfumeController(PerfumeService perfumeService,
      PerfumeSearchService perfumeSearchService) {
    this.perfumeService = perfumeService;
    this.perfumeSearchService = perfumeSearchService;
  }

  /**
   * Get a perfume by id.
   *
   * @param perfumeId the id of the perfume,
   * @return a perfume.
   */
  @GetMapping("/perfumes/{perfumeId}")
  @JsonView(value = PerfumeView.class)
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
  @JsonView(value = PerfumeView.class)
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

  @GetMapping("/perfumes/search")
  @JsonView(value = PerfumeView.class)
  public Page<PerfumeDto> searchPerfumes(@RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "genderFilter", required = false) String genderFilter,
      @RequestParam(value = "yearFilter", required = false) String yearFilter,
      @RequestParam(value = "perfumeTypeFilter", required = false) String perfumeTypeFilter,
      @RequestParam(value = "companyFilter", required = false) String companyFilter,
      @PageableDefault(size = 9) Pageable pageable) {
    log.info("searching perfumes");

    List<Filter> filterList = map(genderFilter);
    List<Filter> yearFilterList = map(yearFilter);
    List<Filter> perfumeTypeFilterList = map(perfumeTypeFilter);
    List<Filter> companyFilterList = map(companyFilter);

    return perfumeSearchService.search(pageable, query, filterList, yearFilterList, perfumeTypeFilterList, companyFilterList);
  }

  private List<Filter> map(String filters) {
    List<Filter> filterList = new ArrayList<>();

    if (filters == null) {
      return filterList;
    }

    if (filters.contains(";")) {
      for (String filter : filters.split(";")) {
        filterList.addAll(this.mapValues(filter));
      }
    } else {
      filterList.addAll(this.mapValues(filters));
    }
    return filterList.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private List<Filter> mapValues(String filter) {
    List<Filter> filters = new ArrayList<>();
    // check if filter string is valid
    if (!filter.contains(":")) {
      return filters;
    }

    if (filter.startsWith(",")) {
      filter = filter.replaceFirst(",", "");
    }

    final String[] values = filter.split(":");
    if (values[1].contains(",")) {
      for (String f : values[1].split(",")) {
        filters.add(new Filter(values[0], f));
      }
    } else {
      filters.add(new Filter(values[0], values[1]));
    }

    return filters;
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
