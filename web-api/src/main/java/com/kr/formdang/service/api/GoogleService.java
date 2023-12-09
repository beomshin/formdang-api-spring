package com.kr.formdang.service.api;

import com.kr.formdang.model.external.google.GoogleLoginDto;
import com.kr.formdang.model.external.google.GoogleLoginRequestDto;

public interface GoogleService {

    GoogleLoginDto googleOAuth(GoogleLoginRequestDto requestDto) throws Exception;
}
