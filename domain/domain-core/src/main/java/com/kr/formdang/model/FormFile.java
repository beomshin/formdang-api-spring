package com.kr.formdang.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
@Builder
public class FormFile {

    private MultipartFile file;
    private Integer order;
    private Integer type;

    public FormFile(MultipartFile file, Integer order, Integer type) {
        this.file = file;
        this.order = order;
        this.type = type;
    }

}
