package com.github.remusselea.scentdb.data;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "perfume_notes")
public class PerfumeNote implements Serializable {

  @Id
  @Column(name = "perfume_id")
  private long perfumeId;

  @Id
  @ManyToOne
  @JoinColumn(name = "note_id")
  private Note note;

  @Column(name = "notes_type")
  private char noteType;
}
