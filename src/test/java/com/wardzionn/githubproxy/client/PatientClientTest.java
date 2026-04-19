package com.wardzionn.githubproxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wardzionn.githubproxy.dto.responses.GithubResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureWireMock(port = 0)
public class PatientClientTest {

    @Autowired
    private PatientClient patientClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getRepositoryDetails_returnsExpectedData() throws JsonProcessingException {
        String mockResponse = objectMapper.writeValueAsString(
                GithubResponseDto.builder()
                        .full_name("octocat/Hello-World")
                        .description("My first repository on GitHub!")
                        .clone_url("https://github.com/octocat/hello-world.git")
                        .stargazers_count(2000)
                        .created_at(LocalDateTime.of(2011, 1, 26, 19, 1, 12))
                        .build()
        );

        stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
                .willReturn(okJson(mockResponse)));

        GithubResponseDto result = patientClient.getRepositoryDetails("octocat", "Hello-World");

        Assertions.assertEquals("octocat/Hello-World", result.getFull_name());
        Assertions.assertEquals("My first repository on GitHub!", result.getDescription());
        Assertions.assertEquals("https://github.com/octocat/hello-world.git", result.getClone_url());
        Assertions.assertEquals(2000, result.getStargazers_count());
    }

    @Test
    void getRepositoryDetails_when503ThenOk_returnsBody() throws JsonProcessingException {
        String mockResponse = objectMapper.writeValueAsString(
                GithubResponseDto.builder()
                        .full_name("octocat/hello-world")
                        .description("Test repository")
                        .clone_url("https://github.com/octocat/hello-world.git")
                        .stargazers_count(123)
                        .created_at(LocalDateTime.of(2026, 12, 12, 12, 0, 0))
                        .build()
        );

        stubFor(get(urlEqualTo("/repos/octocat/hello-world"))
                .inScenario("retry scenario")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse().withStatus(503))
                .willSetStateTo("second call"));
        stubFor(get(urlEqualTo("/repos/octocat/hello-world"))
                .inScenario("retry scenario")
                .whenScenarioStateIs("second call")
                .willReturn(aResponse().withStatus(503))
                .willSetStateTo("third call"));
        stubFor(get(urlEqualTo("/repos/octocat/hello-world"))
                .inScenario("retry scenario")
                .whenScenarioStateIs("third call")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponse)));

        GithubResponseDto result = patientClient.getRepositoryDetails("octocat", "hello-world");

        assertThat(result).isNotNull();
        assertThat(result.getFull_name()).isEqualTo("octocat/hello-world");
        assertThat(result.getDescription()).isEqualTo("Test repository");
        assertThat(result.getClone_url()).isEqualTo("https://github.com/octocat/hello-world.git");
        assertThat(result.getStargazers_count()).isEqualTo(123);
        assertThat(result.getCreated_at()).isEqualTo(LocalDateTime.of(2026, 12, 12, 12, 0, 0));

        verify(3, getRequestedFor(urlEqualTo("/repos/octocat/hello-world")));
    }

    @Test
    void shouldReturnNullFromFallbackWhenRepoNotFound() {
        stubFor(get(urlEqualTo("/repos/octocat/down-repo"))
                .willReturn(aResponse().withStatus(404)));
        GithubResponseDto response =
                patientClient.getRepositoryDetails("octocat", "down-repo");

        assertThat(response).isNull();
        verify(1, getRequestedFor(urlEqualTo("/repos/octocat/down-repo")));
    }
}
