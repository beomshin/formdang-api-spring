package com.kr.formdang.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class GlobalFile {

    private String path;
    private String oriName;
    private String newName;
    private Long size;

    public GlobalFile(MultipartFile file, String path) {
        this.path = path;
        this.size = file.getSize();
        this.oriName = file.getOriginalFilename();
        String[] arr = path.split("/");
        this.newName = arr[arr.length - 1];
    }
}
