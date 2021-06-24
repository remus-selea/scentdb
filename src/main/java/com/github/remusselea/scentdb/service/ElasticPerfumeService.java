package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.model.document.Perfume;
import com.github.remusselea.scentdb.repo.ElasticPerfumeRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticPerfumeService {

  private final ElasticPerfumeRepository elasticPerfumeRepository;

  public Perfume createPerfume(Perfume perfume) {
    return elasticPerfumeRepository.save(perfume);
  }

  public Optional<Perfume> getPerfume(String id) {
    return elasticPerfumeRepository.findById(id);
  }

  public void deletePerfume(String id) {
    elasticPerfumeRepository.deleteById(id);
  }

  public Iterable<Perfume> insertBulk(List<Perfume> perfumes) {
    return elasticPerfumeRepository.saveAll(perfumes);
  }

  public Page<Perfume> getPerfumesByTitle(String name, Pageable pageable) {
    return elasticPerfumeRepository.findByTitle(name, pageable);
  }

  public List<Perfume> getPerfumesByTitleContaining(String name) {
    return elasticPerfumeRepository.findByTitleContaining(name);
  }

  public List<Perfume> getProductsByNameUsingAnnotation(String name) {
    return elasticPerfumeRepository.findAllByTitleUsingAnnotations(name);
  }


}
