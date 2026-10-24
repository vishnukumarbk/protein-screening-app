package com.nuclera.protein.screening.app.controller;

import com.nuclera.protein.screening.app.dto.Experiment;
import com.nuclera.protein.screening.app.service.ScreeningService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping("/test/{message}")
    public String ping(@PathVariable(name = "message") final String message) {
        return "Message received " + message + " at " + Instant.now();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Experiment createExperiment(@Valid @RequestBody final Experiment experiment) {
        return screeningService.createExperiment(experiment);
    }

}
