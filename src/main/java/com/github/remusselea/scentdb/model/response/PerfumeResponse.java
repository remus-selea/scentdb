package com.github.remusselea.scentdb.model.response;

import com.github.remusselea.scentdb.model.response.perfume.PerfumeWrapper;
import com.github.remusselea.scentdb.model.response.note.NoteWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeResponse {

    private NoteWrapper noteWrapper;

    private PerfumeWrapper perfumeWrapper;

}
