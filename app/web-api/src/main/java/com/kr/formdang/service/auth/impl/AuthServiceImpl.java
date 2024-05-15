package com.kr.formdang.service.auth.impl;

import com.kr.formdang.service.auth.dto.FormUser;
import com.kr.formdang.service.auth.dto.JwtTokenAuthUser;
import com.kr.formdang.provider.JwtTokenProvider;
import com.kr.formdang.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.key}")
    private String key;

    @Override
    public boolean validate(String token) {
        if (StringUtils.isEmpty(token) || !JwtTokenProvider.validate(parse(token), key)) {
            log.error("인증 토큰 유효성 검사 실패");
            return false;
        }
        return true;
    }

    @Override
    public FormUser generateAuthUser(String token) {
        Map<String, Object> claims = JwtTokenProvider.getClaims(parse(token), key);
        long id = Long.parseLong((String) claims.get("id"));
        String name = String.valueOf(claims.get("name"));
        return JwtTokenAuthUser.instance(id, name);
    }

    @Override
    public String parse(String text) {
        if (StringUtils.isEmpty(text)) return null;
        return text.startsWith("Bearer ") ? text.substring(7) : text;
    }

}
