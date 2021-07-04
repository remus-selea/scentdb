package com.github.remusselea.scentdb.model.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Indexed(index = "scentdb-notes-index")
@Entity(name = "Note")
@Table(name = "notes")
@Getter
@Setter
public class Note implements Serializable {

  @Id
  @GeneratedValue
  @Column(name = "note_id")
  private long noteId;

  @FullTextField(analyzer = "edge_ngram_analyzer", searchAnalyzer = "edge_ngram_search_analyzer")
  @Column(name = "note_name", unique = true)
  private String noteName;

  @KeywordField
  @Column(name = "img_path")
  private String imgPath;

  @FullTextField
  @Column(name = "description", columnDefinition = "TEXT")
  private String description;


  public Note() {
    /*
     * The JPA specification requires that all persistent classes have a no-arg constructor.
     */
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
