package com.kr.formdang.controller;

import com.kr.formdang.model.net.request.FormSubmitRequest;
import com.kr.formdang.model.root.DefaultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FormController {

    @PostMapping(value = "/form/submit")
    public ResponseEntity submitForm(@Valid @RequestBody FormSubmitRequest request) {
        log.debug("{}", request);
        return ResponseEntity.ok().body(new DefaultResponse());
    }

}
