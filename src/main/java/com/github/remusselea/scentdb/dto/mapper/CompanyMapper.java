package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.model.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

  @Mapping(target = "companyId", source = "id")
  CompanyDto companyToCompanyDto(Company company);

  @Mapping(target = "id", source = "companyId")
  Company companyDtoToCompany(CompanyDto companyDto);

}
