package com.github.remusselea.scentdb.repo;

import com.github.remusselea.scentdb.model.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

  boolean existsNoteByNoteName(String noteName);

}
