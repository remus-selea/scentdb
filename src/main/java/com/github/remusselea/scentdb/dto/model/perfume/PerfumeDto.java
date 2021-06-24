package com.github.remusselea.scentdb.dto.model.perfume;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
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

  private Type perfumeType;

  private String bottleSizes;

  private PerfumerDto perfumer;

  private String description;

  @JsonProperty("images")
  private List<PerfumeImageDto> perfumeImageDtoList;

}
