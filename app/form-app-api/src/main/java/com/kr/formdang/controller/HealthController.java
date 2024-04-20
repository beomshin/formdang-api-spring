package com.kr.formdang.controller;

import com.kr.formdang.dto.DefaultResponse;
import com.kr.formdang.dto.RootResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HealthController {


    /**
     * 토큰 유횽성 검사 API
     * 본 서버에서는 NGINX를 통해 인증서버를 통해 인증 후 넘어온다.
     */
    @GetMapping("/admin/validate")
    public ResponseEntity<RootResponse> auth() {
        return ResponseEntity.ok().body(new DefaultResponse());
    }

    /**
     * 헬스 체크 API
     * @return
     */
    @GetMapping("/public/health")
    public ResponseEntity<RootResponse> health() {
        log.info("헬스 요청");
        return ResponseEntity.ok().body(new DefaultResponse());
    }
}
