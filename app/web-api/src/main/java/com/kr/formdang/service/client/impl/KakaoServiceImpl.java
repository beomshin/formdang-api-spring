package com.kr.formdang.service.client.impl;

import com.kr.formdang.external.HttpFormClient;
import com.kr.formdang.external.dto.kakao.KakaoLoginResponse;
import com.kr.formdang.external.dto.kakao.KakaoTokenRequest;
import com.kr.formdang.external.dto.kakao.KakaoTokenResponse;
import com.kr.formdang.service.client.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private final HttpFormClient kakaoClient;
    @Override
    public KakaoLoginResponse kakaoOAuth(KakaoTokenRequest request) throws Exception {
        KakaoTokenResponse kakaoTokenResponse = kakaoClient.requestToken(request.build(), KakaoTokenResponse.class);
        return kakaoClient.requestUserInfo(kakaoTokenResponse.getAccessToken(), KakaoLoginResponse.class);
    }
}
