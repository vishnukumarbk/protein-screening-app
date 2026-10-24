package com.nuclera.protein.screening.app.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
public class ScreeningControllerTest {

    @Autowired
    MockMvcTester mockMvc;

    @Test
    public void testPing() {
        Assertions.assertThat(mockMvc.get().uri("/protein-screening/api/test/hello")
                        .servletPath("/protein-screening"))
                .hasStatus(200)
                .bodyText().startsWith("Message received ");
    }

}
