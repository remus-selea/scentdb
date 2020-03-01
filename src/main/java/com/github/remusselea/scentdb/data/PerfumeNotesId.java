package com.github.remusselea.scentdb.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class PerfumeNotesId  implements Serializable {

    @Column(name="perfume_id")
    private Long perfumeId;

    @Column(name="note_id")
    private Long noteId;

    public PerfumeNotesId(Long perfumeId, Long noteId) {
        this.perfumeId = perfumeId;
        this.noteId = noteId;
    }

    public PerfumeNotesId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        PerfumeNotesId that = (PerfumeNotesId) o;
        return Objects.equals(perfumeId, that.perfumeId) &&
                Objects.equals(noteId, that.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(perfumeId, noteId);
    }
}
