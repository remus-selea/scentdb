package com.github.remusselea.scentdb.mapping;

import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.model.PerfumeRequest;
import com.github.remusselea.scentdb.model.perfume.PerfumeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PerfumeMapperCustomizer.class})
public interface PerfumeMapper {

  @Mapping(target = "perfumeNoteDtoList", ignore = true)
  PerfumeDto perfumeToPerfumeDto(Perfume perfume);

  @Mapping(target = "perfumeId", source = "perfumeDto.perfumeId")
  @Mapping(target = "title", source = "perfumeDto.title")
  @Mapping(target = "brand", source = "perfumeDto.brand")
  @Mapping(target = "launchYear", source = "perfumeDto.launchYear")
  @Mapping(target = "gender", source = "perfumeDto.gender")
  @Mapping(target = "perfumer", source = "perfumeDto.perfumer")
  @Mapping(target = "description", source = "perfumeDto.description")
  @Mapping(target = "imgPath", source = "perfumeDto.imgPath")
  @Mapping(target = "perfumeNotes", ignore = true)
  Perfume perfumeRequestToPerfume(PerfumeRequest perfumeRequest);

}
