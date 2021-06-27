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

@Entity(name = "Perfumer")
@Table(name = "perfumers")
@Getter
@Setter
public class Perfumer {

  @Id
  @Column(name = "perfumer_id")
  @GeneratedValue
  private long id;

  @Column(name = "name")
  private String name;

  @Column(name = "details", columnDefinition = "TEXT")
  private String details;

  @Column(name = "image_Path")
  private String imagePath;

  @OneToMany(
      mappedBy = "perfumer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY
  )
  private Set<Perfume> perfumes;

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
