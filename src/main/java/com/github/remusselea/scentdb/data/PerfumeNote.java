package com.github.remusselea.scentdb.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "perfume_notes")
public class PerfumeNote implements Serializable {

    @Id
    @Column(name="perfume_id")
    private long perfumeId;

    @Id
    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;

    @Column(name = "notes_type")
    private char noteType;
}
