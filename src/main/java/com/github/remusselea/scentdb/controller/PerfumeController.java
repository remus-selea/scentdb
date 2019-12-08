package com.github.remusselea.scentdb.controller;

import com.github.remusselea.scentdb.model.Perfume;
import com.github.remusselea.scentdb.service.MockPerfumes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerfumeController {

    private MockPerfumes mockPerfumes;

    public PerfumeController(MockPerfumes mockPerfumes) {
        this.mockPerfumes = mockPerfumes;
    }

    @GetMapping("/perfume")
    public Perfume getPerfume(){
        return mockPerfumes.getPerfume();
    }
}
