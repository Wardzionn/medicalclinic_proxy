package com.wardzionn.githubproxy.service;

import com.wardzionn.githubproxy.client.DoctorClient;
import com.wardzionn.githubproxy.dto.DoctorDto;
import com.wardzionn.githubproxy.dto.PageDto;
import com.wardzionn.githubproxy.dto.responses.DoctorResponseDto;
import com.wardzionn.githubproxy.mapper.DoctorDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorClient doctorClient;
    private final DoctorDtoMapper doctorDtoMapper;

    public PageDto<DoctorDto> getDoctors(String specialization, Integer page, Integer size) {
        log.info("Requesting doctors with specialization '{}' [page={}, size={}]", specialization, page, size);
        PageDto<DoctorResponseDto> doctorsPage = doctorClient.getDoctors(specialization, page, size);
        return doctorDtoMapper.toDoctorPage(doctorsPage);
    }
}
