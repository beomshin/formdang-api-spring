package com.kr.formdang.service.client;

import com.kr.formdang.external.dto.kakao.KakaoLoginResponse;
import com.kr.formdang.external.dto.kakao.KakaoTokenRequest;


public interface KakaoService {

    KakaoLoginResponse kakaoOAuth(KakaoTokenRequest requestDto) throws Exception;
}
