package com.github.remusselea.scentdb.model.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {

  @JsonProperty("name")
  private String noteName;

  @JsonProperty("imgPath")
  private String imgPath;

}
