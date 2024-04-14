package com.kr.formdang.dto.res;


import com.kr.formdang.root.DefaultResponse;
import com.kr.formdang.service.file.dto.S3File;
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
