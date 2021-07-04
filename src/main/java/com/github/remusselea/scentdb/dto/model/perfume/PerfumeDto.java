package com.github.remusselea.scentdb.dto.model.perfume;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.dto.view.View;
import com.github.remusselea.scentdb.dto.view.View.CompanyView;
import com.github.remusselea.scentdb.dto.view.View.PerfumeView;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeDto {

  @JsonView(View.Basic.class)
  private Long perfumeId;

  @JsonView(View.Basic.class)
  private String title;

  @JsonView(View.Basic.class)
  @JsonProperty("perfumeNotes")
  private List<PerfumeNoteDto> perfumeNoteDtoList;

  @JsonView(View.Basic.class)
  private String launchYear;

  @JsonView(View.Basic.class)
  private Gender gender;

  @JsonView(View.Basic.class)
  private Type perfumeType;

  @JsonView(View.Basic.class)
  private String bottleSizes;

  @JsonView(View.Basic.class)
  private String description;

  @JsonView({PerfumeView.class, CompanyView.class})
  private PerfumerDto perfumer;

  @JsonView({PerfumeView.class})
  private CompanyDto company;

  @JsonView(View.Basic.class)
  @JsonProperty("images")
  private List<PerfumeImageDto> perfumeImageDtoList;

}
