package com.wardzionn.githubproxy.exception.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wardzionn.githubproxy.exception.BaseApplicationException;
import com.wardzionn.githubproxy.exception.ExceptionDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler {

    private final ObjectMapper objectMapper;

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
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String message = extractMessage(ex);
        log.error("FeignException: status={}, message={}", ex.status(), message, ex);
        return ResponseEntity.status(status).body(ExceptionDto.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build());
    }

    private String extractMessage(FeignException ex) {
        String body = ex.contentUTF8();
        if (body == null || body.isBlank()) {
            return ex.getMessage();
        }
        try {
            JsonNode node = objectMapper.readTree(body);
            if (node.hasNonNull("message")) {
                return node.get("message").asText();
            }
            return body;
        } catch (Exception parseFailure) {
            return body;
        }
    }

}
