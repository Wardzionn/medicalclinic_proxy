package com.wardzionn.githubproxy.client;

import com.wardzionn.githubproxy.configuration.FeignConfig;
import com.wardzionn.githubproxy.dto.responses.GithubResponseDto;
import com.wardzionn.githubproxy.fallback.GithubClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        value = "patient_client",
        name = "patient_client",
        configuration = FeignConfig.class,
        fallbackFactory = GithubClientFallbackFactory.class
)
public interface PatientClient {
    @RequestMapping(method = RequestMethod.GET, value = "/repos/{owner}/{repo}", produces = "application/json")
    GithubResponseDto getRepositoryDetails(@PathVariable("owner") String owner, @PathVariable("repo") String repo);

}
