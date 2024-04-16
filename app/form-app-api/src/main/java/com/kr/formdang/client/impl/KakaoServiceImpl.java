package com.kr.formdang.client.impl;

import com.kr.formdang.external.KakaoClient;
import com.kr.formdang.external.dto.kakao.KakaoLoginResponse;
import com.kr.formdang.external.dto.kakao.KakaoTokenRequest;
import com.kr.formdang.external.dto.kakao.KakaoTokenResponse;
import com.kr.formdang.client.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private final KakaoClient kakaoClient;

    @Override
    public KakaoLoginResponse kakaoOAuth(KakaoTokenRequest requestDto) throws Exception {
        KakaoTokenResponse kakaoTokenResponse = (KakaoTokenResponse) kakaoClient.requestToken(requestDto.build());
        return (KakaoLoginResponse) kakaoClient.requestUserInfo(kakaoTokenResponse.getAccessToken());
    }
}
