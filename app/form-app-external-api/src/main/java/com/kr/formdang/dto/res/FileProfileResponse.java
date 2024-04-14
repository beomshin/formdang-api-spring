package com.kr.formdang.dto.res;

import com.kr.formdang.utils.file.dto.GlobalFile;
import com.kr.formdang.root.DefaultResponse;
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
