package com.kr.formdang.service.client;

import com.kr.formdang.external.dto.google.GoogleLoginResponse;
import com.kr.formdang.external.dto.google.GoogleTokenRequest;

public interface GoogleService {

    GoogleLoginResponse googleOAuth(GoogleTokenRequest request) throws Exception;
}
