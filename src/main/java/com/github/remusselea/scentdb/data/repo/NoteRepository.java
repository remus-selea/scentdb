package com.github.remusselea.scentdb.data.repo;

import com.github.remusselea.scentdb.data.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
