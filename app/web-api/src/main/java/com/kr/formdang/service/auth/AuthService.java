package com.kr.formdang.service.auth;

import com.kr.formdang.model.user.FormUser;

public interface AuthService {

    boolean validate(String token);

    FormUser generateAuthUser(String token);

    String parse(String text);
}
