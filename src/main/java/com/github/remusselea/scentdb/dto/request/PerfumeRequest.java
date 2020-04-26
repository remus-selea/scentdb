package com.github.remusselea.scentdb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
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
