package com.wardzionn.githubproxy.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long doctorId;
    private Long patientId;
}