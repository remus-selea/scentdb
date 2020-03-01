package com.github.remusselea.scentdb.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Notes {

    @Id
    @Column(name="note_id")
    @GeneratedValue
    private Long noteId;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "notes")
    private List<PerfumeNotes> perfumes;


}
