package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.dto.request.PerfumerModel;
import com.github.remusselea.scentdb.model.entity.Perfumer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class, PerfumeImageMapper.class,
    PerfumeMapper.class, PerfumeMapperCustomizer.class})
public interface PerfumerMapper {


  @Mapping(target = "perfumerId", source = "id")
  @Mapping(target = "perfumeDtoList", source = "perfumes", qualifiedByName = "perfumeWithoutPerfumersAndCompany")
  @Mapping(target = "company", qualifiedByName = "companyWithoutPerfumesAndPerfumers")
  PerfumerDto perfumerToPerfumerDto(Perfumer perfumer);

  @Mapping(target = "id", source = "perfumerId")
  Perfumer perfumerDtoToPerfumer(PerfumerDto perfumerDto);

  @Mapping(target = "id", source = "perfumerId")
  Perfumer perfumerModelToPerfumer(PerfumerModel perfumerModel);

}
