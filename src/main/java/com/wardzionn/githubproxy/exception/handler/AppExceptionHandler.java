package com.wardzionn.githubproxy.exception.handler;

import com.wardzionn.githubproxy.exception.BaseApplicationException;
import com.wardzionn.githubproxy.exception.ExceptionDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(BaseApplicationException.class)
    public ResponseEntity<ExceptionDto> handleBaseException(BaseApplicationException ex) {
        log.error("BaseApplicationException: status={}, message={}", ex.getStatus(), ex.getMessage(), ex);
        return ResponseEntity.status(ex.getStatus()).body(ExceptionDto.builder()
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExceptionDto> handleFeignException(FeignException ex) {
        log.error("FeignException: status={}, message={}", ex.status(), ex.getMessage(), ex);
        return ResponseEntity.status(ex.status()).body(ExceptionDto.builder()
                .status(ex.status())
                .error(HttpStatus.valueOf(ex.status()).getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

}
