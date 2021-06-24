package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.request.PerfumeRequest;
import com.github.remusselea.scentdb.model.entity.Perfume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PerfumeMapperCustomizer.class})
public interface PerfumeMapper {

  @Mapping(target = "perfumeNoteDtoList", ignore = true)
  @Mapping(target = "perfumer", ignore = true)
  PerfumeDto perfumeToPerfumeDto(Perfume perfume);

  @Mapping(target = "perfumeId", source = "perfumeDto.perfumeId")
  @Mapping(target = "title", source = "perfumeDto.title")
  @Mapping(target = "brand", source = "perfumeDto.brand")
  @Mapping(target = "launchYear", source = "perfumeDto.launchYear")
  @Mapping(target = "gender", source = "perfumeDto.gender")
  @Mapping(target = "perfumeType", source = "perfumeDto.perfumeType")
  @Mapping(target = "bottleSizes", source = "perfumeDto.bottleSizes")
  @Mapping(target = "description", source = "perfumeDto.description")
  @Mapping(target = "perfumeNotes", ignore = true)
  Perfume perfumeRequestToPerfume(PerfumeRequest perfumeRequest);

}
