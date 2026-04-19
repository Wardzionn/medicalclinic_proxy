package com.wardzionn.githubproxy.exception.external;

import com.wardzionn.githubproxy.exception.BaseApplicationException;
import org.springframework.http.HttpStatus;

public class ExternalApiException extends BaseApplicationException {
    public ExternalApiException(String message, int status) {
        super(HttpStatus.valueOf(status), message);
    }
}
