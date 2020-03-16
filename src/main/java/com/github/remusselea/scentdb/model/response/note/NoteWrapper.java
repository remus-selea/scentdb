package com.github.remusselea.scentdb.model.response.note;

import com.github.remusselea.scentdb.model.response.perfume.PerfumeNoteDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteWrapper {

    private long id;

    List<PerfumeNoteDto> noteDtos;

}
