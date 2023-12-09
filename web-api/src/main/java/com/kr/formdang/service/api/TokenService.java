package com.kr.formdang.service.api;

import com.kr.formdang.model.external.auth.JwtTokenResponse;

public interface TokenService {

    JwtTokenResponse getLoginToken(String id, String key);
}
