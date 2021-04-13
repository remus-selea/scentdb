-- NOTES
INSERT INTO notes (note_id, note_name, img_path) VALUES (1, 'Leather', 'http://localhost:8321/scentdb/v1/images/notes/leather.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (2, 'Opoponax', 'http://localhost:8321/scentdb/v1/images/notes/opoponax.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (3, 'Oregano', 'http://localhost:8321/scentdb/v1/images/notes/oregano.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (4, 'Pepper', 'http://localhost:8321/scentdb/v1/images/notes/pepper.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (5, 'Labdanum', 'http://localhost:8321/scentdb/v1/images/notes/labdanum.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (6, 'Bergamot', 'http://localhost:8321/scentdb/v1/images/notes/bergamot.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (7, 'Amber', 'http://localhost:8321/scentdb/v1/images/notes/amber.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (8, 'Incense', 'http://localhost:8321/scentdb/v1/images/notes/incense.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (9, 'Patchouli', 'http://localhost:8321/scentdb/v1/images/notes/patchouli.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (10, 'Agarwood', 'http://localhost:8321/scentdb/v1/images/notes/agarwood.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (11, 'Sandalwood', 'http://localhost:8321/scentdb/v1/images/notes/sandalwood.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (12, 'Rose', 'http://localhost:8321/scentdb/v1/images/notes/rose.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (13, 'Nutmeg', 'http://localhost:8321/scentdb/v1/images/notes/nutmeg.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (14, 'Tuberose', 'http://localhost:8321/scentdb/v1/images/notes/tuberose.jpg');
INSERT INTO notes (note_id, note_name, img_path) VALUES (15, 'Peach', 'http://localhost:8321/scentdb/v1/images/notes/peach.jpg');


INSERT INTO perfumes(perfume_id,BRAND,DESCRIPTION,GENDER,IMG_PATH,LAUNCH_YEAR,PERFUMER,TITLE) VALUES(1,'Amouage','Interlude Man is a popular perfume by Amouage for men and was released in 2012. The scent is smoky-spicy. Projection and longevity are above-average. It is being marketed by Oman Perfumery',
                                                                                                     'MEN','http://localhost:8321/scentdb/v1/images/perfumes/amouage-im.jpg','2012','Pierre Negrin','Amouage Interlude Men');

INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,1,'b');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,2,'m');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,3,'t');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,4,'t');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,5,'m');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,6,'t');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,7,'m');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,8,'m');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,9,'b');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,10,'b');
INSERT INTO perfume_notes (perfume_perfume_id, note_note_id, notes_type) VALUES (1,11,'b');
