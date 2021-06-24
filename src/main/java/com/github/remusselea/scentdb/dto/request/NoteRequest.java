package com.github.remusselea.scentdb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteRequest {

  @JsonProperty("id")
  private Long noteId;

  @JsonProperty("name")
  private String noteName;

  @JsonProperty("imgPath")
  private String imgPath;

  @JsonProperty("description")
  private String description;

}
