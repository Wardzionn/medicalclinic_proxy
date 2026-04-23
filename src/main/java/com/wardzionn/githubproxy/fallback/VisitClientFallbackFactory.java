package com.wardzionn.githubproxy.fallback;

import com.wardzionn.githubproxy.client.VisitClient;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.VisitDto;
import com.wardzionn.githubproxy.dto.VisitSearchCriteria;
import com.wardzionn.githubproxy.exception.BaseApplicationException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VisitClientFallbackFactory implements FallbackFactory<VisitClient> {
    @Override
    public VisitClient create(Throwable cause) {
        return new VisitClient() {
            @Override
            public PageDto<VisitDto> getVisits(VisitSearchCriteria criteria) {
                log.info("[VisitClient fallback] getVisits {}", criteria, cause);
                return null;
            }

            @Override
            public VisitDto bookVisit(Long visitId, Long patientId) {
                log.warn("[VisitClient fallback] bookVisit failed - visitId: {}, patientId: {}", visitId, patientId, cause);
                throw propagate(cause);
            }

            @Override
            public PageDto<VisitDto> cancelVisit(Long visitId) {
                log.warn("[VisitClient fallback] cancelVisit failed - visitId: {}", visitId, cause);
                throw propagate(cause);
            }
        };
    }

    private static RuntimeException propagate(Throwable cause) {
        if (cause instanceof FeignException feignException) {
            return feignException;
        }
        return new BaseApplicationException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Visit service is temporarily unavailable, try again later: " + cause.getMessage());
    }
}
