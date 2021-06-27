package com.github.remusselea.scentdb.dto.model.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.dto.view.View;
import com.github.remusselea.scentdb.dto.view.View.CompanyView;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDto {

  @JsonView(View.Basic.class)
  private Long companyId;

  @JsonView(View.Basic.class)
  private String name;

  @JsonView(View.Basic.class)
  private String website;

  @JsonView(View.Basic.class)
  private String description;

  @JsonView(View.Basic.class)
  private CompanyType companyType;

  @JsonView(View.Basic.class)
  private String imagePath;

  @JsonView(CompanyView.class)
  @JsonProperty("perfumers")
  private List<PerfumerDto> perfumerDtoList;

  @JsonView(CompanyView.class)
  @JsonProperty("perfumes")
  private List<PerfumeDto> perfumeDtoList;

}
