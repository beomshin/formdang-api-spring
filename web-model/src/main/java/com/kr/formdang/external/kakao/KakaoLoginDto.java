package com.kr.formdang.external.kakao;

import com.kr.formdang.external.kakao.dto.KakaoAcount;
import com.kr.formdang.external.kakao.dto.KakaoProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KakaoLoginDto {

    private String id;
    private String connectedAt;
    private KakaoProperties properties;
    private KakaoAcount kakaoAccount;

}
