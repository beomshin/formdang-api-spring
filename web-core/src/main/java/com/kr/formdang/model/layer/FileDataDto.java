package com.kr.formdang.model.layer;

import com.kr.formdang.model.common.GlobalFile;
import com.kr.formdang.model.net.request.FileListRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDataDto {

    private MultipartFile file;
    private Integer order;
    private Integer type;
    private GlobalFile awsFile;

    public FileDataDto(FileListRequest fileListRequest, int idx) {
        this.file = fileListRequest.getFiles().get(idx);
        this.order = fileListRequest.getOrders().get(idx);
        this.type = fileListRequest.getTypes().get(idx);
    }

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
