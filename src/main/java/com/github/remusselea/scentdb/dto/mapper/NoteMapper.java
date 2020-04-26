package com.github.remusselea.scentdb.dto.mapper;

import com.github.remusselea.scentdb.dto.model.note.NoteDto;
import com.github.remusselea.scentdb.dto.request.NoteRequest;
import com.github.remusselea.scentdb.model.entity.Note;
import com.github.remusselea.scentdb.model.entity.PerfumeNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {NoteMapperCustomizer.class})
public interface NoteMapper {

  @Mapping(source = "perfumeNote.note.noteName", target = "noteName")
  @Mapping(source = "perfumeNote.note.imgPath", target = "imgPath")
  NoteDto perfumeNoteToNoteDto(PerfumeNote perfumeNote);

  @Mapping(source = "noteName", target = "noteName")
  @Mapping(source = "imgPath", target = "imgPath")
  NoteDto noteToNoteDto(Note note);

  Note convertNoteRequestToNote(NoteRequest noteRequest);

}
