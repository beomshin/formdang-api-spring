package com.kr.formdang.service.api;

import com.kr.formdang.external.auth.JwtTokenResponse;

public interface TokenService {

    JwtTokenResponse getLoginToken(String id, String name, String profile, String key);
}
