package com.kr.formdang.model.net.response;

import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.root.DefaultResponse;
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
