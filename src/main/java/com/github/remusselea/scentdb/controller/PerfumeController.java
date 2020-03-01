package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.data.Perfumes;
import com.github.remusselea.scentdb.model.PerfumeDTO;
import com.github.remusselea.scentdb.service.MockPerfumes;
import com.github.remusselea.scentdb.service.PerfumesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerfumeController {

    private MockPerfumes mockPerfumes;

    private PerfumesService perfumesService;

    public PerfumeController(MockPerfumes mockPerfumes, PerfumesService perfumesService) {
        this.mockPerfumes = mockPerfumes;
        this.perfumesService = perfumesService;
    }

    @GetMapping("/perfume")
    public Perfumes getPerfumeById(){

        Perfumes perfumes = perfumesService.getPerfumeById(1L);

        return perfumes;
    }
}
