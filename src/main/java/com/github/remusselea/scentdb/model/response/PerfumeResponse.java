package com.github.remusselea.scentdb.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.remusselea.scentdb.model.response.note.NoteDto;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeWrapper;
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
  private List<PerfumeWrapper> perfumeWrapperList;

}
