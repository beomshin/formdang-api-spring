package com.kr.formdang.service.auth;

import com.kr.formdang.service.auth.dto.FormUser;

public interface AuthService {

    boolean validate(String token);

    FormUser generateAuthUser(String token);

    String parse(String text);
}
