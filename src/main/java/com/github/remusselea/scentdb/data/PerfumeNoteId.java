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

  private PerfumeNoteId() {

  }

  public PerfumeNoteId(Long perfumeId, Long noteId) {
    this.perfumeId = perfumeId;
    this.noteId = noteId;
  }

  public int hashCode() {
    return Objects.hash(perfumeId, noteId);
  }

  public boolean equals(Object object) {
    if (object instanceof PerfumeNoteId) {
      PerfumeNoteId that = (PerfumeNoteId) object;
      return (this.perfumeId == that.perfumeId && this.noteId == that.noteId);
    }

    return false;
  }

}
