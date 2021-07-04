package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.perfume.PerfumeDto;
import com.github.remusselea.scentdb.dto.request.PerfumeModel;
import com.github.remusselea.scentdb.dto.request.PerfumeRequest;
import com.github.remusselea.scentdb.model.entity.Perfume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {PerfumerMapper.class, CompanyMapper.class,
    PerfumeImageMapper.class, PerfumeMapperCustomizer.class})
public interface PerfumeMapper {

  @Mapping(target = "perfumeNoteDtoList", ignore = true)
  @Mapping(target = "perfumeImageDtoList", source = "perfumeImages")
  PerfumeDto perfumeToPerfumeDto(Perfume perfume);

  @Named("perfumeWithoutPerfumersAndCompany")
  @Mapping(target = "perfumeNoteDtoList", ignore = true)
  @Mapping(target = "perfumeImageDtoList", source = "perfumeImages")
  @Mapping(target = "perfumer", ignore = true)
  @Mapping(target = "company", ignore = true)
  PerfumeDto perfumeToPerfumeDtoWithoutPerfumersAndCompany(Perfume perfume);

  @Named("perfumeWithoutCompany")
  @Mapping(target = "perfumeImageDtoList", source = "perfumeImages")
  @Mapping(target = "perfumeNoteDtoList", ignore = true)
  @Mapping(target = "perfumer.company", ignore = true)
  @Mapping(target = "perfumer.perfumerId", source = "perfumer.id")
  @Mapping(target = "company", ignore = true)
  PerfumeDto perfumeToPerfumeDtoWithoutCompany(Perfume perfume);

  @Mapping(target = "perfumeId", source = "perfumeId")
  @Mapping(target = "title", source = "title")
  Perfume perfumeModelToPerfume(PerfumeModel perfumeModel);

}
