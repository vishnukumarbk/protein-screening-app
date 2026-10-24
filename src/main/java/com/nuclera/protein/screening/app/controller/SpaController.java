package com.nuclera.protein.screening.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    // Angular routes such as /experiments only exist in the browser, so a refresh or bookmark of one must load
    // index.html and let the Angular router take over. Literal mappings like /api and /error still take precedence,
    // and paths with a dot (static files) are not matched.
    @GetMapping("/{route:[^.]*}")
    public String forwardToApp() {
        return "forward:/index.html";
    }
}
