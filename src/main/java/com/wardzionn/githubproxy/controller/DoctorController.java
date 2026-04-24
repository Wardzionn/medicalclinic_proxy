package com.wardzionn.githubproxy.controller;

import com.wardzionn.githubproxy.dto.DoctorDto;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageDto<DoctorDto> getDoctors(
            @RequestParam(value = "specialization", required = false) String specialization,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        return doctorService.getDoctors(specialization, page, size);
    }
}
