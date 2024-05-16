package com.kr.formdang.model;

import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.model.AbstractResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class FailResponse extends AbstractResponse {

    public FailResponse(ResultCode code) {
        super(code);
    }
}
