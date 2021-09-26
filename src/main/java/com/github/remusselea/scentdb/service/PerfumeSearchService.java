package com.github.remusselea.scentdb.service;


import com.github.remusselea.scentdb.dto.Filter;
import com.github.remusselea.scentdb.dto.mapper.PerfumeMapper;
import com.github.remusselea.scentdb.dto.model.perfume.Gender;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.model.perfume.Type;
import com.github.remusselea.scentdb.model.entity.Perfume;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
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
public class PerfumeSearchService {

  private final EntityManager entityManager;

  private final PerfumeMapper perfumeMapper;

  public PerfumeSearchService(EntityManager entityManager,
      PerfumeMapper perfumeMapper) {
    this.entityManager = entityManager;
    this.perfumeMapper = perfumeMapper;
  }

  @Transactional(readOnly = true)
  public Page<PerfumeDto> search(Pageable pageable, String query, List<Filter> genderFilterList,
      List<Filter> yearFilterList, List<Filter> perfumeTypeFilterList,
      List<Filter> companyFilterList) {
    Page<PerfumeDto> perfumeResponsePage;

    if ((query == null || query.isBlank()) && genderFilterList.isEmpty()
        && companyFilterList.isEmpty() && perfumeTypeFilterList.isEmpty()
        && yearFilterList.isEmpty()) {
      perfumeResponsePage = searchMatchAll(pageable, query);
    } else {
      perfumeResponsePage = searchTerms(pageable, query, genderFilterList, yearFilterList,
          perfumeTypeFilterList, companyFilterList);
    }

    return perfumeResponsePage;
  }


  @Transactional(readOnly = true)
  public Page<PerfumeDto> searchTerms(Pageable pageable, String query,
      List<Filter> genderFilter, List<Filter> yearFilterList, List<Filter> perfumeTypeFilterList,
      List<Filter> companyFilterList) {

    SearchSession searchSession = Search.session(entityManager);
    SearchScope<Perfume> scope = searchSession.scope(Perfume.class);

    SearchPredicate boolPredicates = formSearchPredicates(query, scope, genderFilter,
        yearFilterList, perfumeTypeFilterList, companyFilterList);
    SearchSort searchSort = getSearchSort(pageable, scope);

    ElasticsearchSearchQuery<Perfume> searchQuery = searchSession.search(Perfume.class)
        .extension(ElasticsearchExtension.get())
        .where(boolPredicates)
        .sort(searchSort)
        .toQuery();

    SearchResult<Perfume> result = searchQuery
        .fetch((int) pageable.getOffset(), pageable.getPageSize());

    return getPerfumeDtoPage(pageable, result);
  }

  private SearchSort getSearchSort(Pageable pageable, SearchScope<Perfume> scope) {
    List<Order> sortOrderList = pageable.getSort().stream().collect(Collectors.toList());
    SearchSortFactory searchSortFactory = scope.sort();
    CompositeSortComponentsStep<?> compositeSortComponentsStep = searchSortFactory.composite();

    for (Order sortOrder : sortOrderList) {
      String fieldToSortBy = sortOrder.getProperty();

      if (fieldToSortBy.equals("bestMatch")) {
        compositeSortComponentsStep.add(searchSortFactory.score());
      } else {
        SortOrder order = SortOrder.DESC;

        if (sortOrder.isAscending()) {
          order = SortOrder.ASC;
        }

        FieldSortOptionsStep<?, ? extends SearchPredicateFactory> fieldSortOptionsStep = searchSortFactory.field(
            fieldToSortBy).order(order);
        compositeSortComponentsStep.add(fieldSortOptionsStep.toSort());
      }

    }

    return compositeSortComponentsStep.toSort();
  }

  @Transactional(readOnly = true)
  public Page<PerfumeDto> searchMatchAll(Pageable pageable, String query) {
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

    return getPerfumeDtoPage(pageable, result);
  }

  @NotNull
  private Page<PerfumeDto> getPerfumeDtoPage(Pageable pageable, SearchResult<Perfume> result) {
    List<Perfume> perfumeList = result.hits();
    List<PerfumeDto> perfumeDtoList = new ArrayList<>(perfumeList.size());

    for (Perfume perfume : perfumeList) {
      PerfumeDto perfumeDto = perfumeMapper.perfumeToPerfumeDto(perfume);
      perfumeDtoList.add(perfumeDto);
    }

    return new PageImpl<>(perfumeDtoList, pageable, result.total().hitCount());
  }

  private SearchPredicate formSearchPredicates(String query, SearchScope<Perfume> searchScope,
      List<Filter> genderFilter, List<Filter> yearFilterList, List<Filter> perfumeTypeFilterList,
      List<Filter> companyFilterList) {
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
    perfumeTypeFilterList.stream().collect(Collectors.groupingBy(Filter::getKey))
        .forEach((key, values) -> {
          BooleanPredicateClausesStep<?> clausesStep = factory.bool();
          values.forEach(
              value -> clausesStep.should(
                  factory.match().field("perfumeType")
                      .matching(Type.valueOf((String) value.getValue()))));

          booleanJunction.must(clausesStep);
        });
  }


  private void prepareCompanyFilterPredicate(BooleanPredicateClausesStep<?> booleanJunction,
      SearchPredicateFactory factory, List<Filter> companyFilterList) {
    if (companyFilterList == null || companyFilterList.isEmpty()) {
      return;
    }
    companyFilterList.stream().collect(Collectors.groupingBy(Filter::getKey))
        .forEach((key, values) -> {
          BooleanPredicateClausesStep<?> clausesStep = factory.bool();
          values.forEach(
              value -> clausesStep.should(
                  factory.match().field("company.companyId")
                      .matching(Long.parseLong((String) value.getValue()))));

          booleanJunction.must(clausesStep);
        });
  }

}
