package com.wardzionn.githubproxy.controller;

import com.wardzionn.githubproxy.dto.RepoDetailsDto;
import com.wardzionn.githubproxy.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/{id}/visits")
    @ResponseStatus(code = HttpStatus.OK)
    public RepoDetailsDto getPatientVisits(@PathVariable("id") Long id) {
        return patientService.getPatientVisits(id);
    }

}
