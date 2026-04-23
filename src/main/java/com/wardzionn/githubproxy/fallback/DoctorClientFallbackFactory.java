package com.wardzionn.githubproxy.fallback;

import com.wardzionn.githubproxy.client.DoctorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorClientFallbackFactory implements FallbackFactory<DoctorClient> {
    @Override
    public DoctorClient create(Throwable cause) {
        return specialization -> {
            log.info("[DoctorClient fallback] getDoctors - specialization: {}", specialization, cause);
            return null;
        };
    }
}
