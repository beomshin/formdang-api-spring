package com.kr.formdang.model.net.response;

import com.kr.formdang.model.common.GlobalFile;
import com.kr.formdang.model.root.DefaultResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileProfileResponse extends DefaultResponse {

    private GlobalFile file;
    private String accessToken;

}
