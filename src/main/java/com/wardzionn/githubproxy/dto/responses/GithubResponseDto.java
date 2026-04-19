package com.wardzionn.githubproxy.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class GithubResponseDto {
    private String full_name;
    private String description;
    private String clone_url;
    private int stargazers_count;
    private LocalDateTime created_at;
}
