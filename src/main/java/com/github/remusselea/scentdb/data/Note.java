package com.github.remusselea.scentdb.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notes")
public class Note {

  @Id
  @Column(name = "note_id")
  private long noteId;

  @Column(name = "note_name")
  private String noteName;

  @Column(name = "img_path")
  private String imgPath;
}
