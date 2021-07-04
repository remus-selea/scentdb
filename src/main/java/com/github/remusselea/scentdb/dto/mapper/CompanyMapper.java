package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.dto.request.CompanyModel;
import com.github.remusselea.scentdb.model.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",  uses = {PerfumerMapper.class, PerfumeImageMapper.class,
    PerfumeMapper.class})
public interface CompanyMapper {

  @Mapping(target = "companyId", source = "companyId")
  @Mapping(target = "companyType", source = "type")
  @Mapping(target = "perfumerDtoList", source = "perfumers")
  @Mapping(target = "perfumeDtoList", source = "perfumes", qualifiedByName = "perfumeWithoutCompany")
  CompanyDto companyToCompanyDto(Company company);


  @Named("companyWithoutPerfumesAndPerfumers")
  @Mapping(target = "companyId", source = "companyId")
  @Mapping(target = "companyType", source = "type")
  @Mapping(target = "perfumerDtoList", ignore = true)
  @Mapping(target = "perfumeDtoList", ignore = true)
  CompanyDto companyToCompanyDtoWithoutPerfumesAndPerfumers(Company company);

  @Mapping(target = "companyId", source = "companyId")
  @Mapping(target = "type", source = "companyType")
  Company companyDtoToCompany(CompanyDto companyDto);

  @Mapping(target = "companyId", source = "companyId")
  @Mapping(target = "type", source = "companyType")
  Company companyModelToCompany(CompanyModel companyModel);

}
