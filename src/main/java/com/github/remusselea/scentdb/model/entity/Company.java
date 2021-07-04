package com.github.remusselea.scentdb.model.entity;

import com.github.remusselea.scentdb.dto.model.company.CompanyType;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Indexed(index = "scentdb-companies-index")
@Entity(name = "Company")
@Table(name = "companies")
@Getter
@Setter
public class Company {

  @GenericField
  @Id
  @Column(name = "company_id")
  @GeneratedValue
  private Long companyId;

  @FullTextField(analyzer = "edge_ngram_analyzer", searchAnalyzer = "edge_ngram_search_analyzer")
  @Column(name = "name")
  private String name;

  @FullTextField
  @Column(name = "website")
  private String website;

  @FullTextField
  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @KeywordField
  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private CompanyType type;

  @GenericField
  @Column(name = "image_path")
  private String imagePath;

  @OneToMany(
      mappedBy = "company",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<Perfumer> perfumers;

  @IndexedEmbedded(includeDepth = 1)
  @OneToMany(
      mappedBy = "company",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<Perfume> perfumes;

  public void addPerfumer(Perfumer perfumer) {
    perfumers.add(perfumer);
    perfumer.setCompany(this);
  }

  public void removePerfumer(Perfumer perfumer) {
    perfumers.remove(perfumer);
    perfumer.setCompany(null);
  }

  public void addPerfume(Perfume perfume) {
    perfumes.add(perfume);
    perfume.setCompany(this);
  }

  public void removePerfume(Perfume perfume) {
    perfumes.remove(perfume);
    perfume.setCompany(null);
  }

}
