package com.kr.formdang.dto.res;

import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.dto.DefaultResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubmitFormResponse extends DefaultResponse {

    private Long fid;

    public SubmitFormResponse(ResultCode code, Long fid) {
        super(code);
        this.fid = fid;
    }

}
