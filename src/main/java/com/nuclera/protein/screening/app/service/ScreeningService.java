package com.nuclera.protein.screening.app.service;

import com.nuclera.protein.screening.app.dto.Experiment;
import com.nuclera.protein.screening.app.entity.ExperimentEntity;
import com.nuclera.protein.screening.app.repository.ScreeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Transactional(readOnly = true)
    public List<Experiment> listExperiments() {
        return screeningRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(ExperimentEntity::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Experiment> findExperiment(final UUID id) {
        return screeningRepository.findById(id).map(ExperimentEntity::toDto);
    }
}
