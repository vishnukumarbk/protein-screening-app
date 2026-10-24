package com.nuclera.protein.screening.app.dto;

import com.nuclera.protein.screening.app.validation.ValidScreeningSequences;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Experiment(
        UUID id,
        @NotBlank String name,
        @NotEmpty @ValidScreeningSequences List<@NotBlank String> sequences,
        Instant createdAt) {

    public Experiment {
        sequences = sequences == null ? null : List.copyOf(sequences);
    }
}
