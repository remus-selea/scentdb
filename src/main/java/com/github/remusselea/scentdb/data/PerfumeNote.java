package com.github.remusselea.scentdb.data;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "perfume_notes")
public class PerfumeNote implements Serializable {

  @EmbeddedId
  private PerfumeNoteId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("perfumeId")
  @JoinColumn(name = "perfume_id")
  private Perfume perfume;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("noteId")
  @JoinColumn(name = "note_id")
  private Note note;

  @Column(name = "notes_type")
  private char noteType;

  public PerfumeNote() {

  }

  public PerfumeNote(Perfume perfume, Note note) {
    this.perfume = perfume;
    this.note = note;
    this.id = new PerfumeNoteId(perfume.getPerfumeId(), note.getNoteId());
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerfumeNote that = (PerfumeNote) o;
    return Objects.equals(this.perfume, that.perfume) && Objects.equals(this.note, that.note);
  }

  public int hashCode() {
    return Objects.hash(this.perfume, this.note);
  }

}
