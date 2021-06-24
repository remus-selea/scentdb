package com.github.remusselea.scentdb.service;


import com.github.remusselea.scentdb.dto.Filter;
import com.github.remusselea.scentdb.repo.ElasticPerfumeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class PerfumeSearchService {

  private ElasticPerfumeRepository elasticPerfumeRepository;

  @Autowired
  public PerfumeSearchService(ElasticPerfumeRepository elasticPerfumeRepository) {
    this.elasticPerfumeRepository = elasticPerfumeRepository;
  }

  public Page search(String query, List<Filter> filters, Pageable pageable) {
    BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();

    if (query != null && !query.isEmpty()) {
      MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(query);
      searchQuery.must(multiMatchQuery);
    }

    prepareFilters(searchQuery, filters);
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    queryBuilder.withQuery(searchQuery).withPageable(pageable);

    Page page = elasticPerfumeRepository.search(queryBuilder.build());
    return page;
  }

  private void prepareFilters(BoolQueryBuilder searchQuery, List<Filter> filters) {
    if (filters == null) {
      return;
    }
    filters.stream().collect(Collectors.groupingBy(Filter::getKey)).forEach((key, values) -> {
      BoolQueryBuilder bool = QueryBuilders.boolQuery();
      values.forEach(value -> bool.should(QueryBuilders.matchQuery(key, value.getValue())));
      searchQuery.must(bool);
    });
  }


}
