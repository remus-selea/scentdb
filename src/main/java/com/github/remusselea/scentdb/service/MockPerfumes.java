package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.model.Gender;
import com.github.remusselea.scentdb.model.NoteDTO;
import com.github.remusselea.scentdb.model.NotesDTO;
import com.github.remusselea.scentdb.model.PerfumeDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockPerfumes {

    public PerfumeDTO getPerfume() {
        PerfumeDTO perfume = new PerfumeDTO();

        perfume.setTitle("Amouage Interlude Man");
        perfume.setBrand("Amouage");
        perfume.setGender(Gender.MEN);
        perfume.setDescription("Amouage presents a new pair of fragrances by the name of Interlude.");
        perfume.setId(1);
        perfume.setLaunchYear("2012");
        perfume.setImgPath("/images/1.jpg");
        perfume.setPerfumer("Pierre Negrin");

        NotesDTO notes = new NotesDTO();
        List<NoteDTO> baseNoteList = new ArrayList<>();
        List<NoteDTO> topNoteList = new ArrayList<>();
        List<NoteDTO> middleNoteList = new ArrayList<>();

        NoteDTO note = new NoteDTO();
        note.setId("123");
        note.setName("Bergamot");
        topNoteList.add(note);
        note = new NoteDTO();
        note.setId("234");
        note.setName("Oregano");
        topNoteList.add(note);
        note = new NoteDTO();
        note.setId("345");
        note.setName("Allspice");
        topNoteList.add(note);

        note = new NoteDTO();
        note.setName("Amber");
        middleNoteList.add(note);
        note = new NoteDTO();
        note.setName("Opoponax");
        middleNoteList.add(note);
        note = new NoteDTO();
        note.setName("Incense");
        middleNoteList.add(note);
        note = new NoteDTO();
        note.setName("Cistus");
        middleNoteList.add(note);

        note = new NoteDTO();
        note.setName("Leather");
        baseNoteList.add(note);
        note = new NoteDTO();
        note.setName("Oud");
        baseNoteList.add(note);
        note = new NoteDTO();
        note.setName("Patchouli");
        baseNoteList.add(note);
        note = new NoteDTO();
        note.setName("Sandalwood");
        baseNoteList.add(note);

        notes.setBaseNotes(baseNoteList);
        notes.setMiddleNotes(middleNoteList);
        notes.setTopNotes(topNoteList);

        perfume.setNotes(notes);

        return perfume;
    }
}
