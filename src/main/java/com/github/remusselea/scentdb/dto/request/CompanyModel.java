package com.github.remusselea.scentdb.dto.request;

import com.github.remusselea.scentdb.dto.model.company.CompanyType;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyModel {

  private Long companyId;

  private String name;

  private String website;

  private String description;

  private CompanyType companyType;

  private String imagePath;

  private List<Long> perfumerIdList;

  private List<Long> perfumeIdList;
}
