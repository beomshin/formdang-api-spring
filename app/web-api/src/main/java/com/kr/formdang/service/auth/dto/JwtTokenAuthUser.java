package com.kr.formdang.service.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenAuthUser implements FormUser {

    private final long id;
    private final String name;

    public static JwtTokenAuthUser instance(long id, String name) {
        return new JwtTokenAuthUser(id, name);
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
