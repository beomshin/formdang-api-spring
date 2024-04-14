package com.kr.formdang.root;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kr.formdang.common.GlobalCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
public class DefaultResponse implements RootResponse {

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final Date time = new Date(); // 날짜
    public String resultCode = GlobalCode.SUCCESS.getCode();
    public String resultMsg = GlobalCode.SUCCESS.getMsg();
    public Boolean success = true;
    @Setter
    public String errorMsg;

    public DefaultResponse(GlobalCode code) {
        this.resultCode = code.getCode();
        this.resultMsg = code.getMsg();
        if (!GlobalCode.SUCCESS.equals(code)) this.success = false;
    }

}
