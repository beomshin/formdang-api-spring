package com.kr.formdang.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenProvider {


    public String getId(String key, String token) {
        return this.getClaims(key,token).get("id").toString();
    }

    public String getName(String key, String token) {
        return this.getClaims(key,token).get("name").toString();
    }

    private Claims getClaims(String key, String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean  validateJwtToken(String key, String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("[토큰 인증 실패 에러] ==================> ");
            return false;
        }
    }
}
