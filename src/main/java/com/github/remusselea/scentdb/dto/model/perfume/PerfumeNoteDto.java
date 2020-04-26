package com.github.remusselea.scentdb.dto.model.perfume;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeNoteDto {

  @JsonProperty("type")
  private String noteType;

  private List<Long> notes = new ArrayList<>();

  public void addNoteId(Long noteId) {
    notes.add(noteId);
  }

}
