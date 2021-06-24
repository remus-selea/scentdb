package com.github.remusselea.scentdb.dto.model.company;

import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.model.entity.Perfume;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDto {

  private long companyId;

  private String name;

  private String website;

  private String description;

  private CompanyType companyType;

  private String imagePath;

  private List<PerfumerDto> perfumerDtoList;

  private List<Perfume> perfumeList;

}
