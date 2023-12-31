package com.kr.formdang.controller;

import com.kr.formdang.model.root.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    @GetMapping("/admin/validate")
    public ResponseEntity health() {
        return ResponseEntity.ok().body(new DefaultResponse());
    }

}
