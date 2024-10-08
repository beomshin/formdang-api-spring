package com.kr.formdang.model.response.form;

import com.kr.formdang.model.response.AbstractResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubmitFormResponse extends AbstractResponse {

    private Long fid;

}
