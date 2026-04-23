package com.wardzionn.githubproxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.VisitDto;
import com.wardzionn.githubproxy.dto.VisitSearchCriteria;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureWireMock(port = 0)
class VisitClientTest {

    @Autowired
    private VisitClient visitClient;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void resetWireMock() {
        reset();
    }

    @Test
    void getVisits_returnsExpectedData() throws JsonProcessingException {
        VisitDto visit = new VisitDto();
        visit.setId(42L);
        visit.setDoctorId(1L);
        visit.setPatientId(null);
        visit.setStartTime(LocalDateTime.of(2026, 6, 1, 10, 0));
        visit.setEndTime(LocalDateTime.of(2026, 6, 1, 10, 30));

        PageDto<VisitDto> page = new PageDto<>(0, 10, 1L, 1L, List.of(visit));
        String body = objectMapper.writeValueAsString(page);

        stubFor(get(urlPathEqualTo("/visits"))
                .willReturn(okJson(body)));

        PageDto<VisitDto> result = visitClient.getVisits(VisitSearchCriteria.forDoctor(1L, true));

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(42L);
        assertThat(result.getContent().get(0).getDoctorId()).isEqualTo(1L);

        verify(1, getRequestedFor(urlPathEqualTo("/visits")));
    }

    @Test
    void getVisits_when503ThenOk_retriesAndReturnsBody() throws JsonProcessingException {
        PageDto<VisitDto> page = new PageDto<>(0, 10, 0L, 0L, List.of());
        String body = objectMapper.writeValueAsString(page);

        stubFor(get(urlPathEqualTo("/visits"))
                .inScenario("visit retry")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse().withStatus(503))
                .willSetStateTo("second"));
        stubFor(get(urlPathEqualTo("/visits"))
                .inScenario("visit retry")
                .whenScenarioStateIs("second")
                .willReturn(aResponse().withStatus(503))
                .willSetStateTo("third"));
        stubFor(get(urlPathEqualTo("/visits"))
                .inScenario("visit retry")
                .whenScenarioStateIs("third")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        PageDto<VisitDto> result = visitClient.getVisits(VisitSearchCriteria.forPatient(7L));

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        verify(3, getRequestedFor(urlPathEqualTo("/visits")));
    }

    @Test
    void getVisits_whenNotFound_fallbackReturnsNull() {
        stubFor(get(urlPathEqualTo("/visits"))
                .willReturn(aResponse().withStatus(404)));

        PageDto<VisitDto> result = visitClient.getVisits(VisitSearchCriteria.forPatient(99L));

        assertThat(result).isNull();
        verify(1, getRequestedFor(urlPathEqualTo("/visits")));
    }

    @Test
    void bookVisit_whenNotFound_fallbackPropagatesFeignException() {
        stubFor(patch(urlEqualTo("/visits/1/patient/2"))
                .willReturn(aResponse().withStatus(404)));

        assertThatThrownBy(() -> visitClient.bookVisit(1L, 2L))
                .isInstanceOf(FeignException.NotFound.class);

        verify(1, patchRequestedFor(urlEqualTo("/visits/1/patient/2")));
    }

    @Test
    void bookVisit_when503Exhausted_fallbackPropagatesFeignException() {
        stubFor(patch(urlEqualTo("/visits/1/patient/2"))
                .willReturn(aResponse().withStatus(503)));

        assertThatThrownBy(() -> visitClient.bookVisit(1L, 2L))
                .isInstanceOf(FeignException.class);

        verify(3, patchRequestedFor(urlEqualTo("/visits/1/patient/2")));
    }

    @Test
    void cancelVisit_whenConflict_fallbackPropagatesFeignException() {
        stubFor(delete(urlEqualTo("/visits/5"))
                .willReturn(aResponse().withStatus(409)));

        assertThatThrownBy(() -> visitClient.cancelVisit(5L))
                .isInstanceOf(FeignException.Conflict.class);

        verify(1, deleteRequestedFor(urlEqualTo("/visits/5")));
    }

    @Test
    void cancelVisit_when503Exhausted_fallbackPropagatesFeignException() {
        stubFor(delete(urlEqualTo("/visits/5"))
                .willReturn(aResponse().withStatus(503)));

        assertThatThrownBy(() -> visitClient.cancelVisit(5L))
                .isInstanceOf(FeignException.class);

        verify(3, deleteRequestedFor(urlEqualTo("/visits/5")));
    }
}
