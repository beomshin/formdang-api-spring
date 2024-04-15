package com.kr.formdang.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JwtTokenProvider {

    private static String key;

    public JwtTokenProvider(
            @Value("${jwt.key}") String jwt_key
    ) {
        key = jwt_key;
    }

    private static String parseJwt(String headerAuth) {
        return headerAuth.startsWith("Bearer ") ? headerAuth.substring(7) : headerAuth;
    }

    public static Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(parseJwt(token)).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
