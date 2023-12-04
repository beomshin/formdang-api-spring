package com.kr.formdang.service;

import com.kr.formdang.external.google.GoogleLoginDto;
import com.kr.formdang.external.google.GoogleLoginRequestDto;

public interface GoogleService {

    GoogleLoginDto googleOAuth(GoogleLoginRequestDto requestDto) throws Exception;
}
