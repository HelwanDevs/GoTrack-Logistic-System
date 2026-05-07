package com.gotrack.user_branch_service.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter
                extends OncePerRequestFilter {

        private static final String TRACE_ID = "traceId";

        private static final int MAX_PAYLOAD_LENGTH = 5000;

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {

                if (isMultipart(request)) {
                        filterChain.doFilter(request, response);
                        return;
                }

                ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request,
                                MAX_PAYLOAD_LENGTH);

                ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

                long startTime = System.currentTimeMillis();

                String traceId = UUID.randomUUID().toString();

                MDC.put(TRACE_ID, traceId);

                String method = request.getMethod();
                String uri = request.getRequestURI();
                String query = request.getQueryString();
                String clientIp = getClientIp(request);

                try {
                        logMultiLine(
                                        "=============================================================",
                                        "[HTTP REQUEST START]",
                                        "traceId=" + traceId,
                                        "method=" + method,
                                        "uri=" + uri,
                                        "query=" + query,
                                        "clientIp=" + clientIp,
                                        "ROLE=" + getRole(request),
                                        "=============================================================");

                        filterChain.doFilter(
                                        wrappedRequest,
                                        wrappedResponse);

                } finally {

                        long executionTime = System.currentTimeMillis() - startTime;

                        int status = wrappedResponse.getStatus();

                        String requestBody = getRequestBody(wrappedRequest);

                        String responseBody = getResponseBody(wrappedResponse);

                        logMultiLine(
                                        "=============================================================",
                                        "[HTTP REQUEST]",
                                        "traceId=" + traceId,
                                        "method=" + method,
                                        "uri=" + uri,
                                        "query=" + query,
                                        "clientIp=" + clientIp,
                                        "status=" + status,
                                        "executionTime=" + executionTime + " ms",
                                        "",
                                        "REQUEST BODY:",
                                        filterBody(requestBody),
                                        "",
                                        "RESPONSE BODY:",
                                        filterBody(responseBody),
                                        "=============================================================");

                        wrappedResponse.copyBodyToResponse();

                        MDC.clear();
                }
        }

        private String getRequestBody(
                        ContentCachingRequestWrapper request) {

                byte[] content = request.getContentAsByteArray();

                if (content.length == 0) {
                        return "[EMPTY]";
                }

                return new String(
                                content,
                                StandardCharsets.UTF_8);
        }

        private String getResponseBody(
                        ContentCachingResponseWrapper response) {

                byte[] content = response.getContentAsByteArray();

                if (content.length == 0) {
                        return "[EMPTY]";
                }

                return new String(
                                content,
                                StandardCharsets.UTF_8);
        }

        private String getClientIp(
                        HttpServletRequest request) {

                String forwarded = request.getHeader("X-Forwarded-For");

                if (StringUtils.hasText(forwarded)) {
                        return forwarded.split(",")[0];
                }

                return request.getRemoteAddr();
        }

        private String getRole(HttpServletRequest request) {
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.split(" ")[1];
                        try {
                                DecodedJWT jwt = JWT.decode(token);
                                String role = jwt.getClaim("role").asString();
                                return role != null ? role : "[NO ROLE CLAIM]";
                        } catch (Exception e) {
                                log.warn("Failed to extract role from JWT: {}", e.getMessage());
                                return "[INVALID TOKEN]";
                        }
                }
                return "[NO TOKEN]";
        }

        private boolean isMultipart(
                        HttpServletRequest request) {

                String contentType = request.getContentType();

                return contentType != null &&
                                contentType.startsWith("multipart/");
        }

        private String filterBody(String body) {

                if (body == null) {
                        return "[NULL]";
                }

                // limit huge payloads
                if (body.length() > MAX_PAYLOAD_LENGTH) {

                        body = body.substring(
                                        0,
                                        MAX_PAYLOAD_LENGTH) + "... [TRUNCATED]";
                }

                // mask passwords
                body = body.replaceAll(
                                "(?i)\"password\"\\s*:\\s*\".*?\"",
                                "\"password\":\"***\"");

                // mask tokens
                body = body.replaceAll(
                                "(?i)\"token\"\\s*:\\s*\".*?\"",
                                "\"token\":\"***\"");

                return body;
        }

        private void logMultiLine(String... lines) {
                for (String line : lines) {
                        if (line != null && !line.trim().isEmpty()) {
                                log.info(line);
                        }
                }
        }
}