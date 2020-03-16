package com.github.remusselea.scentdb.model.response.note;

import com.github.remusselea.scentdb.model.response.perfume.PerfumeNoteDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteWrapper {

  private long id;

  List<PerfumeNoteDto> noteDtos;

}
