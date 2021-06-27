package com.github.remusselea.scentdb.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumerModel {

  private Long perfumerId;

  private String name;

  private String details;

  private String imagePath;

  private Long companyId;

  private List<Long> perfumeIdList;

}
