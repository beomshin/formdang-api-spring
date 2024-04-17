package com.kr.formdang.dto.req;

import com.kr.formdang.dto.DefaultRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignRequest extends DefaultRequest {

    @NotBlank(message = "아이디가 누락되었습니다.")
    private String id;

    @NotBlank(message = "패스워드가 누락되었습니다.")
    private String pw;
}
