package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.model.response.PerfumeResponse;
import com.github.remusselea.scentdb.service.PerfumesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerfumeController {


    private PerfumesService perfumesService;

    public PerfumeController(PerfumesService perfumesService) {
        this.perfumesService = perfumesService;
    }

    @GetMapping("/perfume")
    public PerfumeResponse getPerfumeById() {

        PerfumeResponse perfumeResponse = perfumesService.getPerfumeById(1L);

        return perfumeResponse;
    }
}
