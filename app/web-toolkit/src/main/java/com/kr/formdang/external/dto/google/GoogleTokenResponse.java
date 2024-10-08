package com.kr.formdang.external.dto.google;

import com.kr.formdang.external.dto.ResponseClient;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleTokenResponse implements ResponseClient {
    private String accessToken; // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
    private String expiresIn;   // Access Token의 남은 수명
    private String refreshToken;    // 새 액세스 토큰을 얻는 데 사용할 수 있는 토큰
    private String scope;
    private String tokenType;   // 반환된 토큰 유형(Bearer 고정)
    private String idToken;
}
