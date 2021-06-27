package com.github.remusselea.scentdb.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.view.View;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteResponse {

  @JsonView(View.Basic.class)
  @JsonProperty("notes")
  private Map<Long, NoteDto> noteDtoMap;
}
