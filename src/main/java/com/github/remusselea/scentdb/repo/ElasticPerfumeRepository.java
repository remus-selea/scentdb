package com.github.remusselea.scentdb.repo;

import com.github.remusselea.scentdb.model.document.Perfume;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticPerfumeRepository extends ElasticsearchRepository<Perfume, String> {

  List<Perfume> findByTitleContaining(String title);

  List<Perfume> findByBrandAndPerfumeType(String brand, String type);

  Page<Perfume> findByTitle(String title, Pageable pageable);

  @Query("{\"match\":{\"title\":\"?0\"}}")
  List<Perfume> findAllByTitleUsingAnnotations(String name);
}
