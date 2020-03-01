package com.github.remusselea.scentdb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeDTO {

    private int id;

    private String title;

    private NotesDTO notes;

    private String brand;

    private String launchYear;

    private Gender gender;

    private String perfumer;

    private String description;

    private String imgPath;

}
