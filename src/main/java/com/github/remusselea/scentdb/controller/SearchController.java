package com.github.remusselea.scentdb.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.github.remusselea.scentdb.dto.Filter;
import com.github.remusselea.scentdb.dto.view.View.PerfumeView;
import com.github.remusselea.scentdb.service.SearchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
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
public class SearchController {


  private SearchService searchService;

  public SearchController(SearchService searchService) {
    this.searchService = searchService;
  }

  @GetMapping("/perfumes/search")
  @JsonView(value = PerfumeView.class)
  public Page searchPerfumes(@RequestParam(value = "q", required = false) String query,
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

    return searchService.search(pageable, query, filterList, yearFilterList, perfumeTypeFilterList, companyFilterList);
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
}
