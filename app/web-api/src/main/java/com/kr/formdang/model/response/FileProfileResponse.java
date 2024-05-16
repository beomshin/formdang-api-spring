package com.kr.formdang.model.response;


import com.kr.formdang.model.AbstractResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileProfileResponse extends AbstractResponse {

    private String accessToken;
    private String logoUrl;

}
