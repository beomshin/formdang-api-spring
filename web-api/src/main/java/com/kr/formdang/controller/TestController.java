package com.kr.formdang.controller;

import com.kr.formdang.model.net.request.test.TestRequest;
import com.kr.formdang.model.net.response.test.TestResponse;
import com.kr.formdang.model.root.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @PostMapping("/call/test")
    public ResponseEntity callTest(
            @RequestBody TestRequest request
    ) {
        log.debug("{}", request);
        log.debug("[테스트 콜]");
        return ResponseEntity.ok().body(new TestResponse());
    }
}
