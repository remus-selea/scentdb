package com.github.remusselea.scentdb.mapping;

import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PerfumeMapperCustomizer.class})
public interface PerfumeMapper {

  @Mapping(target = "perfumeId", source = "perfumeId")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "brand", source = "brand")
  @Mapping(target = "launchYear", source = "launchYear")
  @Mapping(target = "gender", source = "gender")
  @Mapping(target = "perfumer", source = "perfumer")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "imgPath", source = "imgPath")
  @Mapping(target = "perfumeNoteDtoList", ignore = true)
  PerfumeWrapper perfumeToPerfumeWrapper(Perfume perfume);

  @Mapping(target = "perfumeId", source = "perfumeId")
  @Mapping(target = "title", source = "title")
  @Mapping(target = "brand", source = "brand")
  @Mapping(target = "launchYear", source = "launchYear")
  @Mapping(target = "gender", source = "gender")
  @Mapping(target = "perfumer", source = "perfumer")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "imgPath", source = "imgPath")
  @Mapping(target = "perfumeNotes", ignore = true)
  Perfume perfumeWrapperToPerfume(PerfumeWrapper perfumeWrapper);

}
