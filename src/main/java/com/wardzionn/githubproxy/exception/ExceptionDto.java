package com.wardzionn.githubproxy.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExceptionDto {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
