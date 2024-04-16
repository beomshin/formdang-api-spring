package com.kr.formdang.dto.auth;

import com.kr.formdang.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;

public class JwtTokenAuthUser implements AuthUser {

    private final long id;
    private final String name;

    public JwtTokenAuthUser(String token) {
        Claims claims = JwtTokenProvider.getClaims(token);
        this.id = Long.parseLong(claims.get("id").toString());
        this.name = claims.get("name").toString();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
