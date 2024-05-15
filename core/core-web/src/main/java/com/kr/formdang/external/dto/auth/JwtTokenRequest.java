package com.kr.formdang.external.dto.auth;

import com.kr.formdang.external.dto.RequestClient;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenRequest implements RequestClient {

    private String id;
    @Setter
    private String auth_key;
    private String name;
    private String profile;

    public static JwtTokenRequest valueOf(long id, String name, String path) {
        return new JwtTokenRequest(id, name, path);
    }

    private JwtTokenRequest(long id, String name, String path) {
        this.id = String.valueOf(id);
        this.name = name;
        this.profile = path;
    }

}
