package com.kr.formdang.external.dto.auth;

import com.kr.formdang.external.dto.RequestClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenRequest implements RequestClient {

    private String id;
    private String auth_key;
    private String name;
    private String profile;

}
