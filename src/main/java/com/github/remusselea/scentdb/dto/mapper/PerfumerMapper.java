package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.model.entity.Perfumer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerfumerMapper {


  @Mapping(target = "perfumerId", source = "id")
  PerfumerDto perfumerToPerfumerDto(Perfumer perfumer);

  @Mapping(target = "id", source = "perfumerId")
  Perfumer perfumerDtoToPerfumer(PerfumerDto perfumerDto);

}
