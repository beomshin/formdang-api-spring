package com.kr.formdang.external.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtTokenResponse {

    private String time; // 날짜
    public String resultCode;
    public String resultMsg;
    public Boolean success;
    private String accessToken;
    private String refreshToken;
    private String expiredTime;

}
