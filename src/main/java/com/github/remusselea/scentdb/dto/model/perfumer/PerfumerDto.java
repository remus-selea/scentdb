package com.github.remusselea.scentdb.dto.model.perfumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.view.View;

import com.github.remusselea.scentdb.dto.view.View.PerfumerView;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumerDto {

  @JsonView(View.Basic.class)
  private long perfumerId;

  @JsonView(View.Basic.class)
  private String name;

  @JsonView(View.Basic.class)
  private String details;

  @JsonView(View.Basic.class)
  private String imagePath;

  @JsonView(PerfumerView.class)
  private CompanyDto company;

  @JsonView(PerfumerView.class)
  @JsonProperty("perfumes")
  private List<PerfumeDto> perfumeDtoList;

}
