package com.kr.formdang.dto.res;

import com.kr.formdang.common.GlobalFile;
import com.kr.formdang.root.DefaultResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse extends DefaultResponse {

    private GlobalFile file;

}
