package com.github.remusselea.scentdb.service;


import com.github.remusselea.scentdb.dto.Filter;
import com.github.remusselea.scentdb.dto.mapper.NoteMapper;
import com.github.remusselea.scentdb.dto.mapper.PerfumeMapper;
import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.model.perfume.Gender;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.model.perfume.Type;
import com.github.remusselea.scentdb.dto.response.PerfumeResponse;
import com.github.remusselea.scentdb.model.entity.Note;
import com.github.remusselea.scentdb.model.entity.Perfume;
import com.github.remusselea.scentdb.model.entity.PerfumeNote;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.backend.elasticsearch.search.query.ElasticsearchSearchQuery;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.SearchSort;
import org.hibernate.search.engine.search.sort.dsl.CompositeSortComponentsStep;
import org.hibernate.search.engine.search.sort.dsl.FieldSortOptionsStep;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.util.common.data.RangeBoundInclusion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SearchService {

  private EntityManager entityManager;

  private PerfumeMapper perfumeMapper;

  private NoteMapper noteMapper;

  public SearchService(EntityManager entityManager,
      PerfumeMapper perfumeMapper, NoteMapper noteMapper) {
    this.entityManager = entityManager;
    this.perfumeMapper = perfumeMapper;
    this.noteMapper = noteMapper;
  }

  @Transactional(readOnly = true)
  public Page<PerfumeResponse> search(Pageable pageable, String query, List<Filter> genderFilterList,
      List<Filter> yearFilterList, List<Filter> perfumeTypeFilterList, List<Filter> companyFilterList) {
    Page<PerfumeResponse> perfumeResponsePage;

    if ((query == null || query.isBlank()) && genderFilterList.isEmpty() && companyFilterList.isEmpty() && perfumeTypeFilterList.isEmpty()) {
      perfumeResponsePage = searchMatchAll(pageable, query);
    } else {
      perfumeResponsePage = searchTerms(pageable, query, genderFilterList, yearFilterList, perfumeTypeFilterList, companyFilterList);
    }

    return perfumeResponsePage;
  }


  @Transactional(readOnly = true)
  public Page<PerfumeResponse> searchTerms(Pageable pageable, String query,
      List<Filter> genderFilter, List<Filter> yearFilterList, List<Filter> perfumeTypeFilterList, List<Filter> companyFilterList) {

    SearchSession searchSession = Search.session(entityManager);
    SearchScope<Perfume> scope = searchSession.scope(Perfume.class);

    SearchPredicate boolPredicates = formSearchPredicates(query, scope, genderFilter,
        yearFilterList, perfumeTypeFilterList, companyFilterList);
    SearchSort searchSort = getSearchSort(pageable, scope);

    SearchResult<Perfume> result = searchSession.search(scope)
        .where(boolPredicates)
        .sort(searchSort)
        .fetch((int) pageable.getOffset(), pageable.getPageSize());

    List<Perfume> perfumeList = result.hits();
    PerfumeResponse perfumeResponse = createPerfumeResponse();
    perfumeList.forEach(perfume -> addInfoToPerfumeResponse(perfumeResponse, perfume));

    return new PageImpl<>(Collections.singletonList(perfumeResponse), pageable,
        result.total().hitCount());
  }

  private SearchSort getSearchSort(Pageable pageable,
      SearchScope<Perfume> scope) {
    List<Order> sortOrderList = pageable.getSort().stream().collect(Collectors.toList());
    SearchSortFactory searchSortFactory = scope.sort();
    CompositeSortComponentsStep<?> compositeSortComponentsStep  = searchSortFactory.composite();

    for (Order sortOrder: sortOrderList) {
      String fieldToSortBy = sortOrder.getProperty();

      if(fieldToSortBy.equals("bestMatch")) {
        compositeSortComponentsStep.add(searchSortFactory.score());
      } else{
        SortOrder order = SortOrder.DESC;

        if(sortOrder.isAscending()){
          order = SortOrder.ASC;
        }

        FieldSortOptionsStep<?, ? extends SearchPredicateFactory> fieldSortOptionsStep = searchSortFactory.field(fieldToSortBy).order(order);
        compositeSortComponentsStep.add(fieldSortOptionsStep.toSort());
      }

    }

    return compositeSortComponentsStep.toSort();
  }

  @Transactional(readOnly = true)
  public Page<PerfumeResponse> searchMatchAll(Pageable pageable, String query) {
    SearchSession searchSession = Search.session(entityManager);
    SearchScope<Perfume> scope = searchSession.scope(Perfume.class);
    SearchSort searchSort = getSearchSort(pageable, scope);

    ElasticsearchSearchQuery<Perfume> searchQuery = searchSession.search(Perfume.class)
        .extension(ElasticsearchExtension.get())
        .where(SearchPredicateFactory::matchAll)
        .sort(searchSort)
        .toQuery();

    SearchResult<Perfume> result = searchQuery
        .fetch((int) pageable.getOffset(), pageable.getPageSize());
    List<Perfume> perfumeList = result.hits();

    PerfumeResponse perfumeResponse = createPerfumeResponse();
    perfumeList.forEach(perfume -> addInfoToPerfumeResponse(perfumeResponse, perfume));

    return new PageImpl<>(Collections.singletonList(perfumeResponse), pageable,
        result.total().hitCount());
  }

  private SearchPredicate formSearchPredicates(String query, SearchScope<Perfume> searchScope,
      List<Filter> genderFilter, List<Filter> yearFilterList, List<Filter> perfumeTypeFilterList, List<Filter> companyFilterList) {
    SearchPredicateFactory factory = searchScope.predicate();

    BooleanPredicateClausesStep<?> booleanJunction = factory.bool();

    prepareTitlePredicate(booleanJunction, factory, query);
    prepareGenderFilterPredicate(booleanJunction, factory, genderFilter);
    prepareYearFilterPredicate(booleanJunction, factory, yearFilterList);
    preparePerfumeTypeFilterPredicate(booleanJunction, factory, perfumeTypeFilterList);
    prepareCompanyFilterPredicate(booleanJunction, factory, companyFilterList);

    return booleanJunction.toPredicate();
  }

  private void prepareGenderFilterPredicate(BooleanPredicateClausesStep<?> booleanJunction,
      SearchPredicateFactory factory, List<Filter> genderFilter) {
    if (genderFilter == null) {
      return;
    }
    genderFilter.stream().collect(Collectors.groupingBy(Filter::getKey)).forEach((key, values) -> {
      BooleanPredicateClausesStep<?> clausesStep = factory.bool();
      values.forEach(
          value -> clausesStep.should(
              factory.match().field("gender").matching(Gender.valueOf((String) value.getValue()))));

      booleanJunction.must(clausesStep);
    });
  }

  private void prepareYearFilterPredicate(BooleanPredicateClausesStep<?> booleanJunction,
      SearchPredicateFactory factory, List<Filter> yearFilterList) {
    if (yearFilterList == null || yearFilterList.size() != 2) {
      return;
    }

    int startYear = Integer.parseInt((String) yearFilterList.get(0).getValue());
    int endYear = Integer.parseInt((String) yearFilterList.get(1).getValue());

    BooleanPredicateClausesStep<?> clausesStep = factory.bool();

    clausesStep.should(factory.range().field("launchYear").between(
        startYear, RangeBoundInclusion.INCLUDED,
        endYear, RangeBoundInclusion.INCLUDED
    ));

    booleanJunction.must(clausesStep);
  }

  private void prepareTitlePredicate(BooleanPredicateClausesStep<?> booleanJunction,
      SearchPredicateFactory factory, String query) {
    if (StringUtils.isBlank(query)) {
      return;
    }
    booleanJunction.must(factory.match().field("title").matching(query).fuzzy(2).toPredicate());
  }

  private void preparePerfumeTypeFilterPredicate(BooleanPredicateClausesStep<?> booleanJunction,
      SearchPredicateFactory factory, List<Filter> perfumeTypeFilterList) {
    if (perfumeTypeFilterList == null || perfumeTypeFilterList.isEmpty()) {
      return;
    }
    perfumeTypeFilterList.stream().collect(Collectors.groupingBy(Filter::getKey)).forEach((key, values) -> {
      BooleanPredicateClausesStep<?> clausesStep = factory.bool();
      values.forEach(
          value -> clausesStep.should(
              factory.match().field("perfumeType").matching(Type.valueOf((String) value.getValue()))));

      booleanJunction.must(clausesStep);
    });
  }



  private void prepareCompanyFilterPredicate(BooleanPredicateClausesStep<?> booleanJunction,
      SearchPredicateFactory factory, List<Filter> companyFilterList) {
    if (companyFilterList == null || companyFilterList.isEmpty()) {
      return;
    }
    companyFilterList.stream().collect(Collectors.groupingBy(Filter::getKey)).forEach((key, values) -> {
      BooleanPredicateClausesStep<?> clausesStep = factory.bool();
      values.forEach(
          value -> clausesStep.should(
              factory.match().field("company.companyId").matching(Long.parseLong((String) value.getValue()))));

      booleanJunction.must(clausesStep);
    });
  }



  /**
   * Creates a {@link PerfumeResponse} and adds an empty list of {@link PerfumeDto} and empty map of
   * {@link Map noteDtoMap} to it.
   *
   * @return the created PerfumeResponse.
   */
  private PerfumeResponse createPerfumeResponse() {
    PerfumeResponse perfumeResponse = new PerfumeResponse();
    // Create and set the empty perfumeDtoList to perfume response
    List<PerfumeDto> perfumeDtoList = new ArrayList<>();
    perfumeResponse.setPerfumeDtoList(perfumeDtoList);
    // Create and set the empty noteDtoMap to perfume response
    Map<Long, NoteDto> noteDtoMap = new HashMap<>();
    perfumeResponse.setNoteDtoMap(noteDtoMap);

    return perfumeResponse;
  }

  /**
   * Adds the data of a {@link Perfume} to a {@link PerfumeResponse}.
   */
  private void addInfoToPerfumeResponse(PerfumeResponse perfumeResponse, Perfume perfume) {
    // map perfume to perfumeDto
    PerfumeDto perfumeDto = perfumeMapper.perfumeToPerfumeDto(perfume);

    // add each mapped perfumeDto into the perfumeDtoList of perfumeResponse
    perfumeResponse.getPerfumeDtoList().add(perfumeDto);

    // map each PerfumeNote to a NoteDto and put it in the noteDtoMap
    for (PerfumeNote perfumeNote : perfume.getPerfumeNotes()) {
      NoteDto noteDto = noteMapper.perfumeNoteToNoteDto(perfumeNote);
      Note note = perfumeNote.getNote();
      perfumeResponse.getNoteDtoMap().put(note.getNoteId(), noteDto);
    }
  }


}
