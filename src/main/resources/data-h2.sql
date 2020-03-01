INSERT INTO perfumes(perfume_id,BRAND,DESCRIPTION,GENDER,IMG_PATH,LAUNCH_YEAR,PERFUMER,TITLE) VALUES(1,'Amouage','description', 'MEN','path','2010','pierre','amouage im');

INSERT INTO notes (note_id, name) VALUES (1, 'Methyl Pamplemousse');
INSERT INTO notes (note_id, name) VALUES (2, 'Bergamot');


INSERT INTO perfume_notes (note_id, perfume_id, notes_type) VALUES (1,1,'general');
INSERT INTO perfume_notes (note_id, perfume_id, notes_type) VALUES (2,1,'base');

/*
INSERT INTO general_notes (perfume_id, note_id) VALUES (1,1);
INSERT INTO general_notes (perfume_id, note_id) VALUES (1,2);

 */