package com.github.remusselea.scentdb.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "PerfumeImage")
@Table(name = "perfume_images")
@Getter
@Setter
public class PerfumeImage {

  @Id
  @Column(name = "perfume_image_id")
  @GeneratedValue
  private Long id;

  @Column(name = "image_path")
  private String imagePath;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "perfume_id", nullable = false)
  private Perfume perfume;

  /**
   * The JPA specification requires that all persistent classes have a no-arg constructor.
   */
  public PerfumeImage() {
  }

}
