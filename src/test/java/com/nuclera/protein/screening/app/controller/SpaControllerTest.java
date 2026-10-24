package com.nuclera.protein.screening.app.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
public class SpaControllerTest {

    @Autowired
    MockMvcTester mockMvc;

    @Test
    public void testAngularRouteLoadsApp() {
        Assertions.assertThat(mockMvc.get().uri("/protein-screening/experiments")
                        .servletPath("/protein-screening"))
                .hasStatus(HttpStatus.OK)
                .hasForwardedUrl("/index.html");
    }

}
