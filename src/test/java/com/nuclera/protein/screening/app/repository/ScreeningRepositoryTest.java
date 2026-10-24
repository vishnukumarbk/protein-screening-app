package com.nuclera.protein.screening.app.repository;

import com.nuclera.protein.screening.app.dto.Experiment;
import com.nuclera.protein.screening.app.entity.ExperimentEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static com.nuclera.protein.screening.app.ExperimentFixtures.VALID_SEQUENCES;

@DataJpaTest
public class ScreeningRepositoryTest {

    @Autowired
    ScreeningRepository screeningRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void testSaveAndFindExperiment() {
        final Experiment experiment = new Experiment(null, "Screen1", VALID_SEQUENCES, null);

        final ExperimentEntity saved = screeningRepository.save(ExperimentEntity.fromDto(experiment));
        entityManager.flush();
        entityManager.clear();

        final Experiment found = screeningRepository.findById(saved.getId()).orElseThrow().toDto();

        Assertions.assertThat(found.id()).isNotNull();
        Assertions.assertThat(found.name()).isEqualTo("Screen1");
        Assertions.assertThat(found.sequences()).containsExactlyElementsOf(VALID_SEQUENCES);
        Assertions.assertThat(found.createdAt()).isNotNull();
    }

}
