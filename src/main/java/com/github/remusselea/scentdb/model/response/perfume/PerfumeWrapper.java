package com.github.remusselea.scentdb.model.response.perfume;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeWrapper {

    private int perfumeId;

    private String title;

    private List<PerfumeNoteDto> perfumeNoteDtos;

    private String brand;

    private String launchYear;

    private Gender gender;

    private String perfumer;

    private String description;

    private String imgPath;

}
