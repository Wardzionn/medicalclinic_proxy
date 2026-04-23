package com.wardzionn.githubproxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.responses.AccountResponseDto;
import com.wardzionn.githubproxy.dto.responses.DoctorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.reset;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureWireMock(port = 0)
class DoctorClientTest {

    @Autowired
    private DoctorClient doctorClient;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void resetWireMock() {
        reset();
    }

    @Test
    void getDoctors_returnsExpectedData() throws JsonProcessingException {
        PageDto<DoctorResponseDto> page = new PageDto<>(
                0, 10, 1L, 1L,
                List.of(DoctorResponseDto.builder()
                        .id(1L)
                        .specialization("CARDIOLOGY")
                        .accountDto(AccountResponseDto.builder()
                                .id(10L)
                                .email("doc@clinic.io")
                                .firstName("John")
                                .lastName("Doe")
                                .build())
                        .build())
        );
        String body = objectMapper.writeValueAsString(page);

        stubFor(get(urlPathEqualTo("/doctors"))
                .willReturn(okJson(body)));

        PageDto<DoctorResponseDto> result = doctorClient.getDoctors("CARDIOLOGY");

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getContent()).hasSize(1);
        DoctorResponseDto doctor = result.getContent().get(0);
        assertThat(doctor.getId()).isEqualTo(1L);
        assertThat(doctor.getSpecialization()).isEqualTo("CARDIOLOGY");
        assertThat(doctor.getAccountDto().getEmail()).isEqualTo("doc@clinic.io");

        verify(1, getRequestedFor(urlPathEqualTo("/doctors")));
    }

    @Test
    void getDoctors_when503ThenOk_retriesAndReturnsBody() throws JsonProcessingException {
        PageDto<DoctorResponseDto> page = new PageDto<>(
                0, 10, 0L, 0L, List.of()
        );
        String body = objectMapper.writeValueAsString(page);

        stubFor(get(urlPathEqualTo("/doctors"))
                .inScenario("doctor retry")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse().withStatus(503))
                .willSetStateTo("second"));
        stubFor(get(urlPathEqualTo("/doctors"))
                .inScenario("doctor retry")
                .whenScenarioStateIs("second")
                .willReturn(aResponse().withStatus(503))
                .willSetStateTo("third"));
        stubFor(get(urlPathEqualTo("/doctors"))
                .inScenario("doctor retry")
                .whenScenarioStateIs("third")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)));

        PageDto<DoctorResponseDto> result = doctorClient.getDoctors(null);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        verify(3, getRequestedFor(urlPathEqualTo("/doctors")));
    }

    @Test
    void getDoctors_whenNotFound_fallbackReturnsNull() {
        stubFor(get(urlPathEqualTo("/doctors"))
                .willReturn(aResponse().withStatus(404)));

        PageDto<DoctorResponseDto> result = doctorClient.getDoctors("UNKNOWN");

        assertThat(result).isNull();
        verify(1, getRequestedFor(urlPathEqualTo("/doctors")));
    }

    @Test
    void getDoctors_when503Exhausted_fallbackReturnsNull() {
        stubFor(get(urlPathEqualTo("/doctors"))
                .willReturn(aResponse().withStatus(503)));

        PageDto<DoctorResponseDto> result = doctorClient.getDoctors("CARDIOLOGY");

        assertThat(result).isNull();
        verify(3, getRequestedFor(urlPathEqualTo("/doctors")));
    }
}
