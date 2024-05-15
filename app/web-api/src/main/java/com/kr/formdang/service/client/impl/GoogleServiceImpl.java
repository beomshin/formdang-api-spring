package com.kr.formdang.service.client.impl;
import com.kr.formdang.external.HttpFormClient;
import com.kr.formdang.service.client.GoogleService;
import com.kr.formdang.external.dto.google.GoogleLoginResponse;
import com.kr.formdang.external.dto.google.GoogleTokenRequest;
import com.kr.formdang.external.dto.google.GoogleTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {

    private final HttpFormClient googleClient;

    @Override
    public GoogleLoginResponse googleOAuth(GoogleTokenRequest request) throws Exception {
        GoogleTokenResponse googleLoginResponse =  googleClient.requestToken(request, GoogleTokenResponse.class);
        return googleClient.requestUserInfo(googleLoginResponse.getIdToken(), GoogleLoginResponse.class);

    }
}
