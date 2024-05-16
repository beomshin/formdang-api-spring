package com.kr.formdang.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kr.formdang.exception.ResultCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
public abstract class AbstractResponse implements RootResponse {

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final Date time = new Date(); // 날짜
    public String resultCode = ResultCode.SUCCESS.getCode();
    public String resultMsg = ResultCode.SUCCESS.getMsg();
    public Boolean success = true;
    @Setter
    public String errorMsg;

    public AbstractResponse(ResultCode code) {
        this.resultCode = code.getCode();
        this.resultMsg = code.getMsg();
        if (!ResultCode.SUCCESS.equals(code)) this.success = false;
    }

}
