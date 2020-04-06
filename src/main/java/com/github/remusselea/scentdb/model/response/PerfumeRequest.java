package com.github.remusselea.scentdb.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.remusselea.scentdb.model.response.note.NoteDto;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeDto;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeRequest {

  @JsonProperty("notes")
  private Map<Long, NoteDto> noteDtoMap;

  @JsonProperty("perfume")
  private PerfumeDto perfumeDto;
}
