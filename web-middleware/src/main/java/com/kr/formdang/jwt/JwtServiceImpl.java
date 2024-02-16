package com.kr.formdang.jwt;


import com.kr.formdang.exception.CustomException;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${token.access-expired-time}")
    private long ACCESS_EXPIRED_TIME;

    @Value("${token.refresh-expired-time}")
    private long REFRESH_EXPIRED_TIME;

    @Value("${token.jwt-key}")
    public String JWT_KEY;


    @Override
    public Long getId(String token) throws CustomException {
        try {
            log.info("■ JWT 토큰 아이디 조회 [getId]");
            String id = jwtTokenProvider.getId(JWT_KEY, parseJwt(token));
            if (org.apache.commons.lang3.StringUtils.isBlank(id)) throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
            return Long.parseLong(id);
        } catch (Exception e) {
            throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
        }
    }

    @Override
    public String getName(String token) throws CustomException {
        try {
            log.info("■ JWT 토큰 이름 조회 [getName]");
            String name = jwtTokenProvider.getName(JWT_KEY, parseJwt(token));
            if (org.apache.commons.lang3.StringUtils.isBlank(name)) throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
            return name;
        } catch (Exception e) {
            throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
        }
    }

    @Override
    public String parseJwt(String headerAuth) throws CustomException {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(headerAuth) &&
                StringUtils.hasText(headerAuth) &&
                headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        } else {
            throw new CustomException(GlobalCode.NOT_EXIST_TOKEN);
        }
    }

}
