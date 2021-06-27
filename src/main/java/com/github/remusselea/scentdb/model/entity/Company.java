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

@Entity(name = "Company")
@Table(name = "companies")
@Getter
@Setter
public class Company {

  @Id
  @Column(name = "company_id")
  @GeneratedValue
  private long id;

  @Column(name = "name")
  private String name;

  @Column(name = "website")
  private String website;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private CompanyType type;

  @Column(name = "imagePath")
  private String imagePath;

  @OneToMany(
      mappedBy = "company",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<Perfumer> perfumers;

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
