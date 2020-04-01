package com.github.remusselea.scentdb.data.repo;

import com.github.remusselea.scentdb.data.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Long> {

}
