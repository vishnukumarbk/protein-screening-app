package com.nuclera.protein.screening.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class ScreeningController {

    @GetMapping("/test/{message}")
    public String ping(@PathVariable(name = "message") final String message) {
        return "Message received " + message + " at " + Instant.now();
    }
}
