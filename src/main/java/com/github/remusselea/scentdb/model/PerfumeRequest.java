package com.github.remusselea.scentdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.remusselea.scentdb.model.note.NoteDto;
import com.github.remusselea.scentdb.model.perfume.PerfumeDto;
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
