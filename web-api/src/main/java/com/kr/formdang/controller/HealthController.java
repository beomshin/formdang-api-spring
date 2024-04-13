package com.kr.formdang.controller;

import com.kr.formdang.root.DefaultResponse;
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
        log.debug("[private 헬스 체크]");
        return ResponseEntity.ok().body(new DefaultResponse());
    }

    @GetMapping("/public/health")
    public ResponseEntity health2() {
        log.debug("[public 헬스 체크]");
        return ResponseEntity.ok().body(new DefaultResponse());
    }

}
