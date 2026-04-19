package com.wardzionn.githubproxy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RepoDetailsDto {
    private String fullName;
    private String description;
    private String cloneUrl;
    private int stars;
    private LocalDateTime created_at;
}
