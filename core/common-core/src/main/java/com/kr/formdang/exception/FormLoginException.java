package com.kr.formdang.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FormLoginException extends FormException {

    private final String url;

    public FormLoginException(ResultCode code, String url) {
        super(code);
        this.url = url;
    }

}
