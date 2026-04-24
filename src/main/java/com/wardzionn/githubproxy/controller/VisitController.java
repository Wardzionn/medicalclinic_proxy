package com.wardzionn.githubproxy.controller;

import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.VisitDto;
import com.wardzionn.githubproxy.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @GetMapping("/patient/{patientId}")
    @ResponseStatus(HttpStatus.OK)
    public PageDto<VisitDto> getPatientVisits(
            @PathVariable("patientId") Long patientId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        return visitService.getPatientVisits(patientId, page, size);
    }

    @GetMapping("/doctor/{doctorId}")
    @ResponseStatus(HttpStatus.OK)
    public PageDto<VisitDto> getDoctorVisits(
            @PathVariable("doctorId") Long doctorId,
            @RequestParam(value = "freeOnly", required = false) Boolean freeOnly,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        return visitService.getDoctorVisits(doctorId, freeOnly, page, size);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDto<VisitDto> searchVisits(
            @RequestParam(value = "specialization", required = false) String specialization,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(value = "freeOnly", required = false) Boolean freeOnly,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        return visitService.searchVisits(specialization, from, to, freeOnly, page, size);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VisitDto bookVisit(
            @PathVariable("id") Long visitId,
            @RequestParam("patientId") Long patientId
    ) {
        return visitService.bookVisit(visitId, patientId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelVisit(@PathVariable("id") Long visitId) {
        visitService.cancelVisit(visitId);
    }
}
