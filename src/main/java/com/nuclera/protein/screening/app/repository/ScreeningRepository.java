package com.nuclera.protein.screening.app.repository;

import com.nuclera.protein.screening.app.entity.ExperimentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ScreeningRepository extends JpaRepository<ExperimentEntity, UUID> {
}
