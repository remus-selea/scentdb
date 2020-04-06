package com.github.remusselea.scentdb.data;

import com.github.remusselea.scentdb.model.response.perfume.Gender;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "perfumes")
public class Perfume implements Serializable {

  @Id
  @Column(name = "perfume_id")
  private long perfumeId;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "perfume")
  private Set<PerfumeNote> perfumeNotes;

  @Column(name = "title")
  private String title;

  @Column(name = "brand")
  private String brand;

  @Column(name = "launch_year")
  private int launchYear;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  private Gender gender;

  @Column(name = "perfumer")
  private String perfumer;

  @Column(name = "description")
  private String description;

  @Column(name = "img_path")
  private String imgPath;

  /**
   * No-args constructor.
   */
  public Perfume() {
  }
}
