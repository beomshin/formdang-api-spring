package com.kr.formdang.external.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenRequest {

    private String id;
    private String auth_key;
    private String name;
    private String profile;

}
