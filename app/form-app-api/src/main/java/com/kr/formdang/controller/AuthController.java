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
public class AuthController {


    /**
     * 토큰 검증 API
     * 토큰 유횽성 검사를 API
     * 본 서버에서는 NGINX를 통해 인증서버를 통해 인증 후 넘어온다.
     * @return
     */
    @GetMapping("/admin/validate")
    public ResponseEntity<RootResponse> health() {
        return ResponseEntity.ok().body(new DefaultResponse());
    }

}
