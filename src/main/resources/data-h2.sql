INSERT INTO perfumes(perfume_id,BRAND,DESCRIPTION,GENDER,IMG_PATH,LAUNCH_YEAR,PERFUMER,TITLE) VALUES(1,'Amouage','description', 'MEN','path','2010','pierre','amouage im');
INSERT INTO perfumes(perfume_id,BRAND,DESCRIPTION,GENDER,IMG_PATH,LAUNCH_YEAR,PERFUMER,TITLE) VALUES(2,'Providence Perfume Co.','description', 'UNISEX','path','2019','Charna Ethier','Drunk on the Moon');


INSERT INTO notes (note_id, note_name, img_path) VALUES (1, 'Leather', 'path1');
INSERT INTO notes (note_id, note_name, img_path) VALUES (2, 'Opoponax', 'path2');
INSERT INTO notes (note_id, note_name, img_path) VALUES (3, 'Oregano', 'path3');
INSERT INTO notes (note_id, note_name, img_path) VALUES (4, 'Pepper', 'path4');
INSERT INTO notes (note_id, note_name, img_path) VALUES (5, 'Labdanum', 'path5');
INSERT INTO notes (note_id, note_name, img_path) VALUES (6, 'Bergamot', 'path6');

INSERT INTO perfume_notes (perfume_note_id, perfume_id, note_id, notes_type) VALUES (1,1,1,'b');
INSERT INTO perfume_notes (perfume_note_id,perfume_id, note_id, notes_type) VALUES (2,1,2,'m');
INSERT INTO perfume_notes (perfume_note_id,perfume_id, note_id, notes_type) VALUES (3,1,3,'t');
INSERT INTO perfume_notes (perfume_note_id,perfume_id, note_id, notes_type) VALUES (4,1,4,'t');
INSERT INTO perfume_notes (perfume_note_id,perfume_id, note_id, notes_type) VALUES (5,1,5,'m');
INSERT INTO perfume_notes (perfume_note_id,perfume_id, note_id, notes_type) VALUES (6,1,6,'t');

INSERT INTO perfume_notes (perfume_note_id,perfume_id, note_id, notes_type) VALUES (7,2,2,'g');
