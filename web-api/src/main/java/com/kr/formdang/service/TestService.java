package com.kr.formdang.service;

import com.kr.formdang.model.net.request.test.TestRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TestService {

    @Transactional
    void callTest(TestRequest request);
}
