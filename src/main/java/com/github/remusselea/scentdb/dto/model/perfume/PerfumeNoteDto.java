package com.github.remusselea.scentdb.dto.model.perfume;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.remusselea.scentdb.dto.view.View;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeNoteDto {

  @JsonView(View.Basic.class)
  @JsonProperty("type")
  private String noteType;

  @JsonView(View.Basic.class)
  private List<Long> notes = new ArrayList<>();

  public void addNoteId(Long noteId) {
    notes.add(noteId);
  }

}
