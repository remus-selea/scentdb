package com.github.remusselea.scentdb.dto.mapper;


import com.github.remusselea.scentdb.dto.model.perfume.PerfumeImageDto;
import com.github.remusselea.scentdb.model.entity.PerfumeImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerfumeImageMapper {

  @Mapping(target = "perfumeImageId", source = "id")
  PerfumeImageDto perfumeImageToPerfumeImageDto(PerfumeImage perfumeImage);

  @Mapping(target = "id", source = "perfumeImageId")
  PerfumeImage perfumeImageDtoToPerfumeImage(PerfumeImageDto perfumeImageDto);
}
