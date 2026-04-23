package com.wardzionn.githubproxy.dto;

import com.wardzionn.githubproxy.exception.BaseApplicationException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class VisitSearchCriteria {

    private final Long patientId;
    private final Long doctorId;
    private final String specialization;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime to;

    private final Boolean freeOnly;

    public static VisitSearchCriteria forPatient(Long patientId) {
        return VisitSearchCriteria.builder()
                .patientId(patientId)
                .build();
    }

    public static VisitSearchCriteria forDoctor(Long doctorId, Boolean freeOnly) {
        return VisitSearchCriteria.builder()
                .doctorId(doctorId)
                .freeOnly(freeOnly)
                .build();
    }

    public static VisitSearchCriteria search(
            String specialization,
            LocalDateTime from,
            LocalDateTime to,
            Boolean freeOnly
    ) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new BaseApplicationException(HttpStatus.BAD_REQUEST, "ERROR_INVALID_TIME_PERIOD");
        }
        return VisitSearchCriteria.builder()
                .specialization(specialization)
                .from(from)
                .to(to)
                .freeOnly(freeOnly)
                .build();
    }
}
