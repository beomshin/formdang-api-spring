package com.kr.formdang.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginException extends Exception{

    private final ResultCode code;
    private final String url;

    public LoginException(ResultCode code, String url) {
        super(code.getMsg());
        this.code = code;
        this.url = url;
    }

}
