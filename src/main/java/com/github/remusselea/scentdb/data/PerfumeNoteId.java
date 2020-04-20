package com.github.remusselea.scentdb.data;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PerfumeNoteId implements Serializable {

  @Column(name = "perfume_id")
  private Long perfumeId;

  @Column(name = "note_id")
  private Long noteId;

  public PerfumeNoteId() {
  }

  public PerfumeNoteId(
      Long perfumeId,
      Long noteId) {
    this.perfumeId = perfumeId;
    this.noteId = noteId;
  }

  public Long getPerfumeId() {
    return perfumeId;
  }

  public void setPerfumeId(Long perfumeId) {
    this.perfumeId = perfumeId;
  }

  public Long getNoteId() {
    return noteId;
  }

  public void setNoteId(Long noteId) {
    this.noteId = noteId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerfumeNoteId that = (PerfumeNoteId) o;
    return Objects.equals(perfumeId, that.perfumeId)
        && Objects.equals(noteId, that.noteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(perfumeId, noteId);
  }
}