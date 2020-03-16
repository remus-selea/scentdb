package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.data.Perfume;
import com.github.remusselea.scentdb.data.repo.PerfumeRepository;
import com.github.remusselea.scentdb.mapping.PerfumeMapper;
import com.github.remusselea.scentdb.model.response.PerfumeResponse;
import com.github.remusselea.scentdb.model.response.perfume.PerfumeWrapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PerfumesService {

    private PerfumeRepository perfumeRepository;

    private PerfumeMapper perfumeMapper;

    public PerfumesService(PerfumeRepository perfumeRepository, PerfumeMapper perfumeMapper) {
        this.perfumeRepository = perfumeRepository;
        this.perfumeMapper = perfumeMapper;
    }

    @Transactional
    public PerfumeResponse getPerfumeById(Long id) {
        Optional<Perfume> perfumes = perfumeRepository.findById(id);
        PerfumeResponse perfumeResponse = new PerfumeResponse();

        if (perfumes.isPresent()) {
            Perfume perfume = perfumes.get();
            PerfumeWrapper perfumeWrapper = perfumeMapper.perfumeToPerfumeWrapper(perfume);
            perfumeResponse.setPerfumeWrapper(perfumeWrapper);

        }

        return perfumeResponse;
    }
}
