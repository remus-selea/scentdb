package com.github.remusselea.scentdb.dto.model.perfume;


import com.fasterxml.jackson.annotation.JsonView;
import com.github.remusselea.scentdb.dto.view.View;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeImageDto {

  @JsonView(View.Basic.class)
  private long perfumeImageId;

  @JsonView(View.Basic.class)
  private String imagePath;

}
