package com.github.remusselea.scentdb.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotesDTO {

    private List<NoteDTO> topNotes;

    private List<NoteDTO> middleNotes;

    private List<NoteDTO> baseNotes;

    private List<NoteDTO> generalNotes;

}
