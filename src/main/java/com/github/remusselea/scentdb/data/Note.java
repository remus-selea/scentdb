package com.github.remusselea.scentdb.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "notes")
public class Note {

    @Id
    @Column(name="note_id")
    private long noteId;

    @Column(name="note_name")
    private String noteName;

    @Column(name="img_path")
    private String imgPath;
}
