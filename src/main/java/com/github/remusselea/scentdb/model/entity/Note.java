package com.github.remusselea.scentdb.model.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Note")
@Table(name = "notes")

@Getter
@Setter
public class Note implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "note_id")
  private long noteId;

  @Column(name = "note_name")
  private String noteName;

  @Column(name = "img_path")
  private String imgPath;

  /**
   * The JPA specification requires that all persistent classes have a no-arg constructor.
   */
  public Note() {
    // The JPA specification requires that all persistent classes have a no-arg constructor.
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Note note = (Note) o;
    return Objects.equals(noteName, note.noteName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(noteName);
  }
}
