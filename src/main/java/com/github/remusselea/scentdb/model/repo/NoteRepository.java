package com.github.remusselea.scentdb.model.repo;

import com.github.remusselea.scentdb.model.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
