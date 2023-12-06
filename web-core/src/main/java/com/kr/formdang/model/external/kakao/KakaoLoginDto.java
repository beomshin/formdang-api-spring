package com.kr.formdang.model.external.kakao;

import com.kr.formdang.model.external.kakao.dto.KakaoAcount;
import com.kr.formdang.model.external.kakao.dto.KakaoProperties;

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
