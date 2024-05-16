package com.kr.formdang.model;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class S3File {

    private String path;
    private String oriName;
    private String newName;
    private Long size;
    private boolean uploadFlag = false;
    @Setter
    private Integer order;
    @Setter
    private Integer type;

    public S3File(String path, Long size, String oriName, boolean uploadFlag) {
        this.path = path;
        this.size = size;
        this.oriName = oriName;
        if (path != null && !path.isEmpty()) {
            String[] arr = path.split("/");
            this.newName = arr[arr.length - 1];
        }
        this.uploadFlag = uploadFlag;
    }

    public boolean isUploadSuccess() {
        return uploadFlag;
    }

    public boolean isLogo() {
        return this.type == 0;
    }

    public boolean isQuestion() {
        return this.type == 1;
    }
}
