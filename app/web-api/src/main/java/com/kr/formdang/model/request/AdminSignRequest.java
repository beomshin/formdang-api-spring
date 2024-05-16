package com.kr.formdang.model.request;

import com.kr.formdang.model.AbstractRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminSignRequest extends AbstractRequest {

    @NotBlank(message = "아이디가 누락되었습니다.")
    private String id;

    @NotBlank(message = "패스워드가 누락되었습니다.")
    private String pw;
}
