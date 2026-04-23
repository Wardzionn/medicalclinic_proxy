package com.wardzionn.githubproxy.service;

import com.wardzionn.githubproxy.client.VisitClient;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.VisitDto;
import com.wardzionn.githubproxy.dto.VisitSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisitService {

    private final VisitClient visitClient;

    public PageDto<VisitDto> getPatientVisits(Long patientId) {
        log.info("Fetching visits for patient {}", patientId);
        return visitClient.getVisits(VisitSearchCriteria.forPatient(patientId));
    }

    public PageDto<VisitDto> getDoctorVisits(Long doctorId, Boolean freeOnly) {
        log.info("Fetching visits for doctor {} [freeOnly={}]", doctorId, freeOnly);
        return visitClient.getVisits(VisitSearchCriteria.forDoctor(doctorId, freeOnly));
    }

    public PageDto<VisitDto> searchVisits(
            String specialization,
            LocalDateTime from,
            LocalDateTime to,
            Boolean freeOnly
    ) {
        VisitSearchCriteria criteria = VisitSearchCriteria.search(specialization, from, to, freeOnly);
        log.info("Searching visits {}", criteria);
        return visitClient.getVisits(criteria);
    }

    public VisitDto bookVisit(Long visitId, Long patientId) {
        log.info("Booking visit {} for patient {}", visitId, patientId);
        return visitClient.bookVisit(visitId, patientId);
    }

    public PageDto<VisitDto> cancelVisit(Long visitId) {
        log.info("Cancelling visit {}", visitId);
        return visitClient.cancelVisit(visitId);
    }
}
