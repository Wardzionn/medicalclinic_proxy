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

    public PageDto<VisitDto> getPatientVisits(Long patientId, Integer page, Integer size) {
        log.info("Fetching visits for patient {} [page={}, size={}]", patientId, page, size);
        return visitClient.getVisits(VisitSearchCriteria.forPatient(patientId, page, size));
    }

    public PageDto<VisitDto> getDoctorVisits(Long doctorId, Boolean freeOnly, Integer page, Integer size) {
        log.info("Fetching visits for doctor {} [freeOnly={}, page={}, size={}]", doctorId, freeOnly, page, size);
        return visitClient.getVisits(VisitSearchCriteria.forDoctor(doctorId, freeOnly, page, size));
    }

    public PageDto<VisitDto> searchVisits(
            String specialization,
            LocalDateTime from,
            LocalDateTime to,
            Boolean freeOnly,
            Integer page,
            Integer size
    ) {
        VisitSearchCriteria criteria = VisitSearchCriteria.search(specialization, from, to, freeOnly, page, size);
        log.info("Searching visits {}", criteria);
        return visitClient.getVisits(criteria);
    }

    public VisitDto bookVisit(Long visitId, Long patientId) {
        log.info("Booking visit {} for patient {}", visitId, patientId);
        return visitClient.bookVisit(visitId, patientId);
    }

    public void cancelVisit(Long visitId) {
        log.info("Cancelling visit {}", visitId);
        visitClient.cancelVisit(visitId);
    }
}
