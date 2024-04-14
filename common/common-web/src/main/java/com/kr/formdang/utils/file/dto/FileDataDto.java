package com.kr.formdang.utils.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDataDto {

    private MultipartFile file;
    private Integer order;
    private Integer type;
    private GlobalFile awsFile;

    public boolean isLogo() {
        return this.type == 0;
    }

    public boolean isQuestion() {
        return this.type == 1;
    }

    public boolean isUploadSuccess() {
        return this.awsFile != null;
    }

}
