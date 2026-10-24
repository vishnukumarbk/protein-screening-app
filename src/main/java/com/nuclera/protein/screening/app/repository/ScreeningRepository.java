package com.nuclera.protein.screening.app.repository;

import com.nuclera.protein.screening.app.entity.ExperimentEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScreeningRepository extends JpaRepository<ExperimentEntity, UUID> {

    // Fetches the sequences in the same query rather than one extra query per experiment
    @EntityGraph(attributePaths = "sequences")
    List<ExperimentEntity> findAllByOrderByCreatedAtDesc();
}
