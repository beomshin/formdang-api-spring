package com.kr.formdang.external.dto.auth;

import com.kr.formdang.external.dto.ResponseClient;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenResponse implements ResponseClient {

    private String time; // 날짜
    public String resultCode;
    public String resultMsg;
    public Boolean success;
    private String accessToken;
    private String refreshToken;
    private String expiredTime;

    public boolean isFail() {
        final String SUCCESS = "0";
        return !resultCode.equals(SUCCESS);
    }

}
