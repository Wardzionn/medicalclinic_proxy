package com.wardzionn.githubproxy.fallback;

import com.wardzionn.githubproxy.client.PatientClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GithubClientFallbackFactory implements FallbackFactory<PatientClient> {
    @Override
    public PatientClient create(Throwable cause) {
        return (owner, repo) -> {
            log.info("[GithubClient fallback] getRepositoryDetails");
            return null;
        };
    }
}