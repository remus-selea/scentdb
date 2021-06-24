package com.github.remusselea.scentdb.controller;


import com.github.remusselea.scentdb.dto.Filter;
import com.github.remusselea.scentdb.service.PerfumeSearchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/scentdb/v1")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class SearchController {
  private PerfumeSearchService perfumeSearchService;


  @Autowired
  public SearchController(PerfumeSearchService perfumeSearchService) {
    this.perfumeSearchService = perfumeSearchService;
  }

  @GetMapping("/perfumes/search")
  public Page searchObjects(@RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "filter", required = false) String filter,
      @PageableDefault(value = 1, page = 0) Pageable pageable) {
    log.info("searching perfumes");
    return this.perfumeSearchService.search(query, this.map(filter), pageable);
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
}
