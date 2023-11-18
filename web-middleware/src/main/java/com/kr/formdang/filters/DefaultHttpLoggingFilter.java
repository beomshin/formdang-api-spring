package com.kr.formdang.filters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kr.formdang.wrapper.RequestBodyWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Enumeration;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@WebFilter(urlPatterns = "/*")
public class DefaultHttpLoggingFilter extends OncePerRequestFilter {
  private final Gson gson = new GsonBuilder().create();

  /**
   * HTTP 로깅 필터
   *
   * @param request
   * @param response
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    if (isIgnore(request)) { // 로깅을 하지 않는 조건
      filterChain.doFilter(request, response);
      return;
    }

    StopWatch stopWatch = new StopWatch();
    String clientIp = request.getRemoteAddr();
    String realClientIp = getRealClientIp(request);

    if (StringUtils.equals(realClientIp, clientIp)) { // IP, 메소드, URL
      log.info("Request: {} [{}] [{}]", realClientIp, request.getMethod(), request.getRequestURL());
    } else {
      log.info("Request: {} → {} [{}] [{}]", realClientIp, clientIp, request.getMethod(), request.getRequestURL());
    }

    if (log.isDebugEnabled()) { // 세션, 헤더 디버깅 처리
      printSession(request.getSession());
      printHeader(request);
    }

    printParameter(request); // 요청 파라미터 로깅

    RequestBodyWrapper wrappedRequest = new RequestBodyWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    printRequestBody(wrappedRequest); // 요청 body 로깅

    stopWatch.start(); // watch start
    filterChain.doFilter(wrappedRequest, wrappedResponse); // 비지니스 로직
    stopWatch.stop(); // watch stop

    log.info("Returned status=[{}] in [{}]ms, charset=[{}]", response.getStatus(), stopWatch.getTotalTimeMillis(), response.getCharacterEncoding());
    printResponseBody(wrappedResponse); // JSON 응답 로깅
    wrappedResponse.copyBodyToResponse();
  }

  /**
   * 응답 내용을 출력한다. JSON만 출력한다.
   *
   * @param response {@link ContentCachingResponseWrapper}
   */
  private void printResponseBody(ContentCachingResponseWrapper response) {
    if (response.getContentAsByteArray().length > 0) {
      try {
        log.info("Response Body: {}"
                , System.lineSeparator() + gson.toJson(
                        gson.fromJson( // string to jsonObject
                                new String(response.getContentAsByteArray(), 0, response.getContentAsByteArray().length, StandardCharsets.UTF_8) // response byte[] to string
                                , JsonObject.class)
                ));
      } catch (JsonSyntaxException e) {
        log.info("Response Body: [NOT JSON]");
      }
    } else {
      log.info("Response Body: [EMPTY]");
    }
  }

  /**
   * 요청 내용을 출력한다.<br>
   * 1. JSON 일경우 JSON 출력<br>
   * 2. JSON이 아닐 경우 그대로 출력
   *
   * @param request {@link RequestBodyWrapper}
   */
  private void printRequestBody(RequestBodyWrapper request) {
    String body = getRequestBody(request); // request stream to String
    if (StringUtils.isNotBlank(body)) {
      try {
        log.info("Request body: {}", System.lineSeparator() + gson.toJson(gson.fromJson(body, JsonObject.class)));
      } catch (JsonSyntaxException e) {
        log.info("Request body: {}", body);
      }
    } else {
      log.info("Request body: [EMPTY]");
    }
  }

  private String getRequestBody(RequestBodyWrapper request) {
    try {
      return IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.warn("can not read request body: {}", e.getMessage(), e);
      return "";
    }
  }

  private void printParameter(HttpServletRequest request) {
    Enumeration<String> parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String name = parameterNames.nextElement();
      log.info("parameter[{}] = {}", name, request.getParameterValues(name));
    }
  }

  private void printHeader(HttpServletRequest request) {
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String name = headerNames.nextElement();
      log.debug("header[{}] = {}", name, request.getHeader(name));
    }
  }

  /**
   * 세션 내용을 출력한다.
   *
   * @param session {@link HttpSession}
   */
  private void printSession(HttpSession session) {
    if (session == null) {
      log.debug("session is null.");
      return;
    }
    LocalDateTime created =
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(session.getCreationTime()), ZoneId.systemDefault());

    log.debug(
        "session id = {}, created={}, interval={}s",
        session.getId(),
        created,
        session.getMaxInactiveInterval());

    Enumeration<String> names = session.getAttributeNames();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      log.debug("session[{}] = {}", name, session.getAttribute(name));
    }
  }

  /**
   * 웹서버를 타고 들어오는 경우 실제 클라이언트의 IP를 알아낸다.<br>
   * 웹서버 쪽에서도 설정이 되어 있어야 한다.
   *
   * @param request {@link HttpServletRequest}
   * @return 실제 클라이언트 아이피
   */
  private String getRealClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  /**
   * 로깅을 하지 않을 조건인지 체크
   *
   * @param request {@link HttpServletRequest}
   * @return {@code true} 로깅을 하지 않음
   */
  private boolean isIgnore(HttpServletRequest request) {
    return false;
  }
}
