package com.kr.formdang.jwt;

import com.kr.formdang.exception.CustomException;

/**
 * 토큰 서비스
 */
public interface JwtService {
    Long getId(String token) throws CustomException; // 토큰 아이디값 조회
    String getName(String token) throws CustomException; // 토큰 이름 조회
    String parseJwt(String headerAuth) throws CustomException; // 토큰 값 조회
    boolean validateToken(String token) throws CustomException;

}
