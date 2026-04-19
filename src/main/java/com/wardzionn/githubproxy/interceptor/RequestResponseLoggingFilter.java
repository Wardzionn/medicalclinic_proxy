package com.wardzionn.githubproxy.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);
        long start = System.nanoTime();
        try {
            chain.doFilter(req, res);
        } finally {
            String path = request.getRequestURI();
            if (request.getQueryString() != null) {
                path += "?" + request.getQueryString();
            }
            log.info("{} {} -> {} {}ms | REQUEST: {} | RESPONSE: {}",
                    request.getMethod(),
                    path,
                    res.getStatus(),
                    (System.nanoTime() - start) / 1_000_000L,
                    utf8(req.getContentAsByteArray()),
                    utf8(res.getContentAsByteArray()));
            res.copyBodyToResponse();
        }
    }

    private static String utf8(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "-";
        }
        String s = new String(bytes, StandardCharsets.UTF_8);
        return s.length() > 4096 ? s.substring(0, 4096) + "..." : s;
    }
}
