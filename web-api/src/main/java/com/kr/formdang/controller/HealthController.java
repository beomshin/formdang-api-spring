package com.kr.formdang.controller;

import com.kr.formdang.model.net.response.test.TestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity health() {
        log.debug("[헬스체크] =============>");
        return ResponseEntity.ok().body(new TestResponse());
    }
}
