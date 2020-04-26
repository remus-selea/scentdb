package com.github.remusselea.scentdb.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "PerfumeNote")
@Table(name = "perfume_notes")
@Getter
@Setter
public class PerfumeNote implements Serializable {

  @EmbeddedId
  private PerfumeNoteId perfumeNoteId;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("perfumeId")
  @JsonIgnore
  private Perfume perfume;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("noteId")
  private Note note;

  @Column(name = "notes_type")
  private char noteType;

  /**
   * The JPA specification requires that all persistent classes have a no-arg constructor.
   */
  public PerfumeNote() {
  }

  /**
   * Constructor to create a {@link PerfumeNote}
   * and set the composite primary key {@link PerfumeNoteId} consisting of the perfumeId and noteId.
   */
  public PerfumeNote(Perfume perfume, Note note, char noteType) {
    this.perfume = perfume;
    this.note = note;
    this.noteType = noteType;
    this.perfumeNoteId = new PerfumeNoteId(perfume.getPerfumeId(), note.getNoteId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerfumeNote that = (PerfumeNote) o;
    return Objects.equals(perfume, that.perfume)
        && Objects.equals(note, that.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(perfume, note);
  }

}
