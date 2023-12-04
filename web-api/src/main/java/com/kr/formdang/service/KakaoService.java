package com.kr.formdang.service;

import com.kr.formdang.external.kakao.KakaoLoginDto;
import com.kr.formdang.external.kakao.KakaoLoginRequestDto;


public interface KakaoService {

    KakaoLoginDto kakaoOAuth(KakaoLoginRequestDto requestDto) throws Exception;
}
