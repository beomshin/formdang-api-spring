package com.kr.formdang.provider;

import com.kr.formdang.common.GlobalCode;
import com.kr.formdang.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenProvider {

    final private static String JWT_KEY = "LNS2CR13KJ7JYB03FNGX8TP73X9KYWOA";

    public static Long getId(String token) throws CustomException {
        try {
            log.debug("■ JWT 토큰 아이디 조회 [getId]");
            String id = getClaims(JWT_KEY, parseJwt(token)).get("id").toString();
            if (id == null || id.isEmpty()) throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
            return Long.parseLong(id);
        } catch (Exception e) {
            throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
        }
    }

    public static String getName(String token) throws CustomException {
        try {
            log.debug("■ JWT 토큰 이름 조회 [getName]");
            String name = getClaims(JWT_KEY, parseJwt(token)).get("name").toString();
            if (name == null || name.isEmpty()) throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
            return name;
        } catch (Exception e) {
            throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
        }
    }

    public static boolean validateToken(String token) throws CustomException {
        try {
            log.debug("■ JWT 토큰 유효성 검사");
            Jwts.parser().setSigningKey(JWT_KEY).parseClaimsJws(parseJwt(token));
            return true;
        } catch (Exception e) {
            log.error("[토큰 인증 실패 에러] ==================> ");
            return false;
        }
    }


    private static String parseJwt(String headerAuth) throws CustomException {
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        } else {
            throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
        }
    }

    private static Claims getClaims(String key, String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
