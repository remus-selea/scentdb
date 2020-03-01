package com.github.remusselea.scentdb.data;

import com.github.remusselea.scentdb.model.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Perfumes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="perfume_id")
    private Long perfumeId;

    @OneToMany(mappedBy = "perfumes")
    private List<PerfumeNotes> notes;

    @Column(name="title")
    private String title;

    @Column(name="brand")
    private String brand;

    @Column(name="launch_year")
    private String launchYear;

    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Gender gender;

    @Column(name="perfumer")
    private String perfumer;

    @Column(name="description")
    private String description;

    @Column(name="img_path")
    private String imgPath;

    public Perfumes() {
    }
}
