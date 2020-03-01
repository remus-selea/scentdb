package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.data.repo.PerfumeRepository;
import com.github.remusselea.scentdb.data.Perfumes;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PerfumesService {

    private PerfumeRepository perfumeRepository;

    public PerfumesService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public Perfumes getPerfumeById(Long id){
        Optional<Perfumes> perfumes = perfumeRepository.findById(id);

        return  perfumes.get();
    }
}
