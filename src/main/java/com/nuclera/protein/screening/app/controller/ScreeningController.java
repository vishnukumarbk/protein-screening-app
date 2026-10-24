package com.nuclera.protein.screening.app.controller;

import com.nuclera.protein.screening.app.dto.Experiment;
import com.nuclera.protein.screening.app.fasta.FastaParser;
import com.nuclera.protein.screening.app.fasta.InvalidFastaException;
import com.nuclera.protein.screening.app.service.ScreeningService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ScreeningController {

    private final ScreeningService screeningService;
    private final Validator validator;

    public ScreeningController(ScreeningService screeningService, Validator validator) {
        this.screeningService = screeningService;
        this.validator = validator;
    }

    @GetMapping("/test/{message}")
    public String ping(@PathVariable(name = "message") final String message) {
        return "Message received " + message + " at " + Instant.now();
    }

    @GetMapping
    public List<Experiment> listExperiments() {
        return screeningService.listExperiments();
    }

    @GetMapping("/{id}")
    public Experiment getExperiment(@PathVariable(name = "id") final UUID id) {
        return screeningService.findExperiment(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "experiment " + id + " not found"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Experiment createExperiment(@Valid @RequestBody final Experiment experiment) {
        return screeningService.createExperiment(experiment);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Experiment createExperimentFromFasta(@RequestParam("name") final String name,
                                                @RequestParam("file") final MultipartFile file) throws IOException {
        final List<String> sequences;
        try {
            sequences = FastaParser.parse(new String(file.getBytes(), StandardCharsets.UTF_8));
        } catch (final InvalidFastaException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        final Experiment experiment = new Experiment(null, name, sequences, null);
        validate(experiment);
        return screeningService.createExperiment(experiment);
    }

    private void validate(final Experiment experiment) {
        final Set<ConstraintViolation<Experiment>> violations = validator.validate(experiment);
        if (!violations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, violations.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .sorted()
                    .collect(Collectors.joining("; ")));
        }
    }

}
