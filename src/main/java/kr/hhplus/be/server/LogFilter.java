package kr.hhplus.be.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@WebFilter(urlPatterns = "/*")
@Slf4j
public class LogFilter implements Filter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper((HttpServletResponse) response);

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            String requestBody = new String(requestWrapper.getContentAsByteArray());
            String responseBody = new String(responseWrapper.getContentAsByteArray());

            Map<String, Object> requestLog = new LinkedHashMap<>();
            requestLog.put("method", requestWrapper.getMethod());
            requestLog.put("uri", requestWrapper.getRequestURI());
            requestLog.put("params", getParameters(requestWrapper));
            requestLog.put("body", requestBody.isEmpty() ? "{}" : objectMapper.readTree(requestBody));

            long duration = System.currentTimeMillis() - startTime;

            Map<String, Object> responseLog = new LinkedHashMap<>();
            requestLog.put("method", requestWrapper.getMethod());
            responseLog.put("uri", requestWrapper.getRequestURI());
            responseLog.put("status", responseWrapper.getStatus());
            responseLog.put("body", responseBody.isEmpty() ? "{}" : objectMapper.readTree(responseBody));
            responseLog.put("duration", duration + "ms");

            log.info("REQUEST: {}", objectMapper.writeValueAsString(requestLog), duration);
            log.info("RESPONSE: {}", objectMapper.writeValueAsString(responseLog));

            responseWrapper.copyBodyToResponse();
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private Map<String, String[]> getParameters(HttpServletRequest request) {
        return Collections.list(request.getParameterNames()).stream()
                .collect(HashMap::new,
                        (map, name) -> map.put(name, request.getParameterValues(name)),
                        HashMap::putAll);
    }
}
