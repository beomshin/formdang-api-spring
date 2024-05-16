package com.kr.formdang.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FormHttpException extends FormException {

    public FormHttpException(ResultCode code) {
        super(code);
    }

}
