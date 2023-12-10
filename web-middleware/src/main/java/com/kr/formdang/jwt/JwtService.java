package com.kr.formdang.jwt;

import com.kr.formdang.exception.CustomException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface JwtService {
    Long getId(String token) throws CustomException;
    String parseJwt(String headerAuth) throws CustomException;
}
