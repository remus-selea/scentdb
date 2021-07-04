package com.github.remusselea.scentdb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.remusselea.scentdb.dto.model.perfume.Gender;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeNoteDto;
import com.github.remusselea.scentdb.dto.model.perfume.Type;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeModel {

  private Long perfumeId;

  private String title;

  private String launchYear;

  private Gender gender;

  private Type perfumeType;

  private String bottleSizes;

  private String description;

  private Long perfumerId;

  private Long companyId;

  @JsonProperty("perfumeNotes")
  private List<PerfumeNoteDto> perfumeNoteDtoList;

}
