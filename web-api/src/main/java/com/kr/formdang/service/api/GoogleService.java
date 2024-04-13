package com.kr.formdang.service.api;

import com.kr.formdang.external.google.GoogleLoginDto;
import com.kr.formdang.external.google.GoogleLoginRequestDto;

public interface GoogleService {

    GoogleLoginDto googleOAuth(GoogleLoginRequestDto requestDto) throws Exception;
}
