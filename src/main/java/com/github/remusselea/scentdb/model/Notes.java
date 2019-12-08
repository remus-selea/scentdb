package com.github.remusselea.scentdb.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Notes {

    private List<Note> topNotes;

    private List<Note> middleNotes;

    private List<Note> baseNotes;

    private List<Note> generalNotes;

}
