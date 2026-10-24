package com.nuclera.protein.screening.app.service;

import com.nuclera.protein.screening.app.dto.Experiment;
import com.nuclera.protein.screening.app.entity.ExperimentEntity;
import com.nuclera.protein.screening.app.repository.ScreeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;

    public ScreeningService(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    @Transactional
    public Experiment createExperiment(final Experiment experiment) {
        return screeningRepository.save(ExperimentEntity.fromDto(experiment)).toDto();
    }
}
