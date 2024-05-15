package com.kr.formdang.service.client.impl;
import com.kr.formdang.service.client.GoogleService;
import com.kr.formdang.external.GoogleClient;
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

    private final GoogleClient googleClient;

    @Override
    public GoogleLoginResponse googleOAuth(GoogleTokenRequest request) throws Exception {
        GoogleTokenResponse googleLoginResponse = (GoogleTokenResponse) googleClient.requestToken(request);
        return (GoogleLoginResponse) googleClient.requestUserInfo(googleLoginResponse.getIdToken());

    }
}
