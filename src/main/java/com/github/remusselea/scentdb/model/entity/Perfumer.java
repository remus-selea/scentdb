package com.github.remusselea.scentdb.model.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Indexed(index = "scentdb-perfumers-index")
@Entity(name = "Perfumer")
@Table(name = "perfumers")
@Getter
@Setter
public class Perfumer {

  @Id
  @Column(name = "perfumer_id")
  @GeneratedValue
  private long id;

  @FullTextField(analyzer = "edge_ngram_analyzer", searchAnalyzer = "edge_ngram_search_analyzer")
  @KeywordField(name ="nameKeyword", sortable = Sortable.YES)
  @Column(name = "name")
  private String name;

  @FullTextField
  @Column(name = "details", columnDefinition = "TEXT")
  private String details;

  @GenericField
  @Column(name = "image_Path")
  private String imagePath;

  @IndexedEmbedded(includeDepth = 1)
  @OneToMany(
      mappedBy = "perfumer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private Set<Perfume> perfumes;

  @IndexedEmbedded(includeDepth = 1)
  @ManyToOne
  @JoinColumn(name="company_id")
  private Company company;

  /**
   * The JPA specification requires that all persistent classes have a no-arg constructor.
   */
  public Perfumer() {
  }

  public void addPerfume(Perfume perfume) {
    perfumes.add(perfume);
    perfume.setPerfumer(this);
  }


  public void removePerfume(Perfume perfume) {
    perfumes.remove(perfume);
    perfume.setPerfumer(null);
  }

}
