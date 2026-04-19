package com.wardzionn.githubproxy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@Setter
@AllArgsConstructor
public class BaseApplicationException extends RuntimeException {
    private final HttpStatus status;

    public BaseApplicationException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    public static BaseApplicationException entityNotFoundException() {
        return new BaseApplicationException(NOT_FOUND, "ERROR_ENTITY_NOT_FOUND");
    }

}
