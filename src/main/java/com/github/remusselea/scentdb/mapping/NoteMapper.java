package com.github.remusselea.scentdb.mapping;

import com.github.remusselea.scentdb.data.PerfumeNote;
import com.github.remusselea.scentdb.model.response.note.NoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {NoteMapperCustomizer.class})
public interface NoteMapper {

  @Mapping(source = "perfumeNote.note.noteName", target = "noteName")
  @Mapping(source = "perfumeNote.note.imgPath", target = "imgPath")
  NoteDto perfumeNoteToNoteDto(PerfumeNote perfumeNote);

}
