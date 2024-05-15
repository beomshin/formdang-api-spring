package com.kr.formdang.dto.res;


import com.kr.formdang.dto.DefaultResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileProfileResponse extends DefaultResponse {

    private String accessToken;
    private String logoUrl;

}
