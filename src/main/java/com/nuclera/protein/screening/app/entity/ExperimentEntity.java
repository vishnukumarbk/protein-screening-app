package com.nuclera.protein.screening.app.entity;

import com.nuclera.protein.screening.app.dto.Experiment;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "experiment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExperimentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    // Protein sequences can be thousands of residues long, so store each as a CLOB rather than VARCHAR(255)
    @ElementCollection
    @CollectionTable(name = "experiment_sequence", joinColumns = @JoinColumn(name = "experiment_id"))
    @OrderColumn(name = "sequence_index")
    @Lob
    @Column(name = "protein_sequence", nullable = false)
    private List<String> sequences = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public ExperimentEntity(final String name, final List<String> sequences) {
        this.name = name;
        this.sequences = new ArrayList<>(sequences);
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public static ExperimentEntity fromDto(final Experiment experiment) {
        return new ExperimentEntity(experiment.name(), experiment.sequences());
    }

    public Experiment toDto() {
        return new Experiment(id, name, sequences, createdAt);
    }
}
