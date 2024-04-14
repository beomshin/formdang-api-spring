package com.kr.formdang.dto.res;

import com.kr.formdang.common.GlobalCode;
import com.kr.formdang.root.DefaultResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubmitFormResponse extends DefaultResponse {

    private Long fid;

    public SubmitFormResponse(GlobalCode code, Long fid) {
        super(code);
        this.fid = fid;
    }

}
