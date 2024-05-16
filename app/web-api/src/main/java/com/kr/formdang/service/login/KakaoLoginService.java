package com.kr.formdang.service.login;

import com.kr.formdang.external.dto.kakao.KakaoLoginResponse;
import com.kr.formdang.external.dto.kakao.KakaoTokenRequest;


public interface KakaoLoginService {

    KakaoLoginResponse kakaoOAuth(KakaoTokenRequest request) throws Exception;
}
