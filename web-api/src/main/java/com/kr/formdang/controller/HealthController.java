package com.kr.formdang.controller;

import com.kr.formdang.model.net.request.test.TestRequest;
import com.kr.formdang.model.net.response.test.TestResponse;
import com.kr.formdang.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HealthController {

    private final TestService testService;

    @GetMapping("/health")
    public ResponseEntity health() {
        log.debug("[헬스체크]");
        return ResponseEntity.ok().body(new TestResponse());
    }

    @PostMapping("/call/test")
    public ResponseEntity callTest(@RequestBody TestRequest request) {
        log.debug("[테스트 요청]");
        testService.callTest(request);
        return ResponseEntity.ok().body(new TestResponse());
    }

}
