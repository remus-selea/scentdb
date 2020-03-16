package com.github.remusselea.scentdb.model.response.perfume;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeNoteDto {

  private String type;

  private List<Long> notes = new ArrayList<>();

  public void addNote(Long noteId) {
    notes.add(noteId);
  }

}
