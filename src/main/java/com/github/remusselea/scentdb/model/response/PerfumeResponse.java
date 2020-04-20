package com.github.remusselea.scentdb.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.remusselea.scentdb.model.note.NoteDto;
import com.github.remusselea.scentdb.model.perfume.PerfumeDto;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeResponse {

  @JsonProperty("notes")
  private Map<Long, NoteDto> noteDtoMap;

  @JsonProperty("perfumes")
  private List<PerfumeDto> perfumeDtoList;

}
