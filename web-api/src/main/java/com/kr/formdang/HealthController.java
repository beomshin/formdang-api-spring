package com.kr.formdang;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        log.debug("[헬스체크] =============>");
        return "SUCCESS";
    }
}
