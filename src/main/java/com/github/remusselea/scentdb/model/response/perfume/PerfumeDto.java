package com.github.remusselea.scentdb.model.response.perfume;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeDto {

  private long perfumeId;

  private String title;

  @JsonProperty("perfumeNotes")
  private List<PerfumeNoteDto> perfumeNoteDtoList;

  private String brand;

  private String launchYear;

  private Gender gender;

  private String perfumer;

  private String description;

  private String imgPath;

}
