package com.kr.formdang.filters;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class DefaultMDCLoggingFilter extends OncePerRequestFilter {

    /**
     * MDC REQUEST ID 설정
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestId = ((HttpServletRequest)request).getHeader("X-RequestID");
        MDC.put("request_id", requestId != null ? requestId : UUID.randomUUID().toString().replaceAll("-", ""));
        filterChain.doFilter(request, response);
        MDC.clear();
    }
}
