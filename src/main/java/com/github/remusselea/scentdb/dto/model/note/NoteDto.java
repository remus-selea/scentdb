package com.github.remusselea.scentdb.dto.model.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.remusselea.scentdb.dto.view.View;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {

  @JsonView(View.Basic.class)
  @JsonProperty("noteId")
  private Long noteId;

  @JsonView(View.Basic.class)
  @JsonProperty("name")
  private String noteName;

  @JsonView(View.Basic.class)
  @JsonProperty("imgPath")
  private String imgPath;

  @JsonView(View.Basic.class)
  private String description;

}
