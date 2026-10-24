package com.nuclera.protein.screening.app.controller;

import com.jayway.jsonpath.JsonPath;
import com.nuclera.protein.screening.app.dto.Experiment;
import com.nuclera.protein.screening.app.entity.ExperimentEntity;
import com.nuclera.protein.screening.app.repository.ScreeningRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.nuclera.protein.screening.app.ExperimentFixtures.INVALID_SEQUENCES;
import static com.nuclera.protein.screening.app.ExperimentFixtures.VALID_SEQUENCES;

@SpringBootTest
@AutoConfigureMockMvc
public class ScreeningControllerTest {

    private static final String NAME = "Screen1";

    @Autowired
    MockMvcTester mockMvc;

    @Autowired
    JsonMapper jsonMapper;

    @Autowired
    ScreeningRepository screeningRepository;

    @Test
    public void testPing() {
        Assertions.assertThat(mockMvc.get().uri("/protein-screening/api/test/hello")
                        .servletPath("/protein-screening"))
                .hasStatus(200)
                .bodyText().startsWith("Message received ");
    }

    @Test
    public void testCreateExperiment() throws UnsupportedEncodingException {
        final MvcTestResult result = postExperiment(VALID_SEQUENCES);

        Assertions.assertThat(result).hasStatus(HttpStatus.CREATED);
        Assertions.assertThat(result).bodyJson().extractingPath("$.name").isEqualTo(NAME);
        Assertions.assertThat(result).bodyJson().extractingPath("$.sequences").asArray()
                .containsExactlyElementsOf(VALID_SEQUENCES);
        Assertions.assertThat(result).bodyJson().extractingPath("$.createdAt").isNotNull();

        final String id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Assertions.assertThat(screeningRepository.findById(UUID.fromString(id)))
                .map(ExperimentEntity::getName)
                .contains(NAME);
    }

    @Test
    public void testCreateExperimentRejectsInvalidSequences() {
        final long countBefore = screeningRepository.count();

        final MvcTestResult result = postExperiment(INVALID_SEQUENCES);

        Assertions.assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(result.getMvcResult().getResolvedException())
                .isInstanceOf(MethodArgumentNotValidException.class);
        Assertions.assertThat(result).bodyJson().extractingPath("$.detail")
                .isEqualTo("sequences must share a common prefix of at least 5 amino acids");
        Assertions.assertThat(screeningRepository.count()).isEqualTo(countBefore);
    }

    @Test
    public void testListExperiments() throws UnsupportedEncodingException {
        final String id = createdId(postExperiment(VALID_SEQUENCES));

        final MvcTestResult result = mockMvc.get().uri("/protein-screening/api")
                .servletPath("/protein-screening")
                .exchange();

        Assertions.assertThat(result).hasStatus(HttpStatus.OK);
        // Newest first
        Assertions.assertThat(result).bodyJson().extractingPath("$[0].id").isEqualTo(id);
        Assertions.assertThat(result).bodyJson().extractingPath("$[0].sequences").asArray()
                .containsExactlyElementsOf(VALID_SEQUENCES);
    }

    @Test
    public void testGetExperiment() throws UnsupportedEncodingException {
        final String id = createdId(postExperiment(VALID_SEQUENCES));

        final MvcTestResult result = getExperiment(id);

        Assertions.assertThat(result).hasStatus(HttpStatus.OK);
        Assertions.assertThat(result).bodyJson().extractingPath("$.id").isEqualTo(id);
        Assertions.assertThat(result).bodyJson().extractingPath("$.name").isEqualTo(NAME);
        Assertions.assertThat(result).bodyJson().extractingPath("$.sequences").asArray()
                .containsExactlyElementsOf(VALID_SEQUENCES);
    }

    @Test
    public void testGetExperimentReturnsNotFoundForUnknownId() {
        final MvcTestResult result = getExperiment(UUID.randomUUID().toString());

        Assertions.assertThat(result).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateExperimentFromFasta() throws IOException {
        final MvcTestResult result = uploadFasta(new ClassPathResource("fasta/valid.fasta").getContentAsByteArray());

        Assertions.assertThat(result).hasStatus(HttpStatus.CREATED);
        Assertions.assertThat(result).bodyJson().extractingPath("$.name").isEqualTo(NAME);
        Assertions.assertThat(result).bodyJson().extractingPath("$.sequences").asArray()
                .containsExactlyElementsOf(VALID_SEQUENCES);
    }

    @Test
    public void testCreateExperimentFromFastaRejectsInvalidSequences() throws IOException {
        final long countBefore = screeningRepository.count();

        final MvcTestResult result = uploadFasta(new ClassPathResource("fasta/invalid.fasta").getContentAsByteArray());

        Assertions.assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(screeningRepository.count()).isEqualTo(countBefore);
    }

    @Test
    public void testCreateExperimentFromFastaRejectsTenSequences() throws IOException {
        final long countBefore = screeningRepository.count();

        final MvcTestResult result = uploadFasta(new ClassPathResource("fasta/ten-sequences.fasta").getContentAsByteArray());

        Assertions.assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(result.getMvcResult().getResolvedException())
                .hasMessageContaining("must contain exactly 20 sequences");
        Assertions.assertThat(result).bodyJson().extractingPath("$.detail")
                .isEqualTo("sequences must contain exactly 20 sequences");
        Assertions.assertThat(screeningRepository.count()).isEqualTo(countBefore);
    }

    @Test
    public void testCreateExperimentFromFastaRejectsMalformedFile() {
        final long countBefore = screeningRepository.count();

        // Sequence data without a '>' header line
        final MvcTestResult result = uploadFasta("MKTAYIAVRNDQGSKLEA\n".getBytes(StandardCharsets.UTF_8));

        Assertions.assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(screeningRepository.count()).isEqualTo(countBefore);
    }

    private MvcTestResult postExperiment(final List<String> sequences) {
        return mockMvc.post().uri("/protein-screening/api")
                .servletPath("/protein-screening")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(new Experiment(null, NAME, sequences, null)))
                .exchange();
    }

    private MvcTestResult getExperiment(final String id) {
        return mockMvc.get().uri("/protein-screening/api/" + id)
                .servletPath("/protein-screening")
                .exchange();
    }

    private static String createdId(final MvcTestResult result) throws UnsupportedEncodingException {
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    private MvcTestResult uploadFasta(final byte[] fasta) {
        return mockMvc.post().uri("/protein-screening/api")
                .multipart()
                .servletPath("/protein-screening")
                .file("file", fasta)
                .param("name", NAME)
                .exchange();
    }

}
