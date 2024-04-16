package com.kr.formdang.external.dto.auth;

import com.kr.formdang.external.dto.ResponseClient;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtTokenResponse implements ResponseClient {

    private String time; // 날짜
    public String resultCode;
    public String resultMsg;
    public Boolean success;
    private String accessToken;
    private String refreshToken;
    private String expiredTime;

}
