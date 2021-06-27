package com.github.remusselea.scentdb.model.entity;

import com.github.remusselea.scentdb.dto.model.perfume.Gender;
import com.github.remusselea.scentdb.dto.model.perfume.Type;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity(name = "Perfume")
@Table(name = "perfumes")
@Getter
@Setter
public class Perfume implements Serializable {

  @Id
  @Column(name = "perfume_id")
  @GeneratedValue
  private Long perfumeId;

  @OneToMany(
      mappedBy = "perfume",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<PerfumeNote> perfumeNotes = new HashSet<>();

  @Column(name = "title")
  private String title;

  @Column(name = "brand")
  private String brand;

  @Column(name = "launch_year")
  private int launchYear;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  private Gender gender;

  @Enumerated(EnumType.STRING)
  @Column(name = "perfume_type")
  private Type perfumeType;

  @Column(name = "bottle_sizes")
  private String bottleSizes;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "perfumer_id")
  private Perfumer perfumer;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToMany(
      mappedBy = "perfume",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<PerfumeImage> perfumeImages = new HashSet<>();

  /**
   * The JPA specification requires that all persistent classes have a no-arg constructor.
   */
  public Perfume() {
    // The JPA specification requires that all persistent classes have a no-arg constructor.
  }

  public void addPerfumeImage(PerfumeImage perfumeImage) {
    perfumeImages.add(perfumeImage);
    perfumeImage.setPerfume(this);
  }


  public void removePerfumeImage(PerfumeImage perfumeImage) {
    perfumeImages.remove(perfumeImage);
    perfumeImage.setPerfume(null);
  }

  public void addNote(Note note, char noteType) {
    PerfumeNote perfumeNote = new PerfumeNote(this, note, noteType);
    perfumeNotes.add(perfumeNote);
  }


  /**
   * Remove al {@link Note} from the Set of {@link PerfumeNote}.
   */
  public void removeNote(Note note) {
    for (Iterator<PerfumeNote> iterator = perfumeNotes.iterator();
        iterator.hasNext(); ) {
      PerfumeNote perfumeNote = iterator.next();

      if (perfumeNote.getPerfume().equals(this)
          && perfumeNote.getNote().equals(note)) {
        iterator.remove();
        perfumeNote.setPerfume(null);
        perfumeNote.setNote(null);
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Perfume post = (Perfume) o;
    return Objects.equals(title, post.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

}
