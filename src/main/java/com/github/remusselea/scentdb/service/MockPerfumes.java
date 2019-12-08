package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.model.Gender;
import com.github.remusselea.scentdb.model.Note;
import com.github.remusselea.scentdb.model.Notes;
import com.github.remusselea.scentdb.model.Perfume;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockPerfumes {

    public Perfume getPerfume() {
        Perfume perfume = new Perfume();

        perfume.setTitle("Amouage Interlude Man");
        perfume.setBrand("Amouage");
        perfume.setGender(Gender.MEN);
        perfume.setDescription("Amouage presents a new pair of fragrances by the name of Interlude.");
        perfume.setId(1);
        perfume.setLaunchYear("2012");
        perfume.setImgPath("/images/1.jpg");
        perfume.setPerfumer("Pierre Negrin");

        Notes notes = new Notes();
        List<Note> baseNoteList = new ArrayList<>();
        List<Note> topNoteList = new ArrayList<>();
        List<Note> middleNoteList = new ArrayList<>();

        Note note = new Note();
        note.setId("123");
        note.setName("Bergamot");
        topNoteList.add(note);
        note = new Note();
        note.setId("234");
        note.setName("Oregano");
        topNoteList.add(note);
        note = new Note();
        note.setId("345");
        note.setName("Allspice");
        topNoteList.add(note);

        note = new Note();
        note.setName("Amber");
        middleNoteList.add(note);
        note = new Note();
        note.setName("Opoponax");
        middleNoteList.add(note);
        note = new Note();
        note.setName("Incense");
        middleNoteList.add(note);
        note = new Note();
        note.setName("Cistus");
        middleNoteList.add(note);

        note = new Note();
        note.setName("Leather");
        baseNoteList.add(note);
        note = new Note();
        note.setName("Oud");
        baseNoteList.add(note);
        note = new Note();
        note.setName("Patchouli");
        baseNoteList.add(note);
        note = new Note();
        note.setName("Sandalwood");
        baseNoteList.add(note);

        notes.setBaseNotes(baseNoteList);
        notes.setMiddleNotes(middleNoteList);
        notes.setTopNotes(topNoteList);

        perfume.setNotes(notes);

        return perfume;
    }
}
