package com.github.remusselea.scentdb.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "perfume_notes")
public class PerfumeNotes {

    @EmbeddedId
    private PerfumeNotesId id;

    @ManyToOne
    @MapsId("perfumeId")
    @JoinColumn(name = "perfume_id")
    private Perfumes perfumes;

    @ManyToOne
    @MapsId("noteId")
    @JoinColumn(name = "note_id")
    private Notes notes;

    @Column
    private String notes_type;
}
