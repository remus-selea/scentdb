package com.github.remusselea.scentdb.model.response.perfume;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PerfumeNoteDto {

    private String type;

    private List<Long> notes = new ArrayList<>();

    public void addNote(Long noteId) {
        notes.add(noteId);
    }

}
