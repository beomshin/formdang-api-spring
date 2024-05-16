package com.kr.formdang.service.login;

import com.kr.formdang.external.dto.google.GoogleLoginResponse;
import com.kr.formdang.external.dto.google.GoogleTokenRequest;

public interface GoogleLoginService {

    GoogleLoginResponse googleOAuth(GoogleTokenRequest request) throws Exception;
}
