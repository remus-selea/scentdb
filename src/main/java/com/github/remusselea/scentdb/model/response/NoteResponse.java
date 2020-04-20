package com.github.remusselea.scentdb.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.remusselea.scentdb.model.note.NoteDto;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteResponse {

  @JsonProperty("notes")
  private Map<Long, NoteDto> noteDtoMap;
}
