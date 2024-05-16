package com.kr.formdang.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileRequest {

    @NotNull
    private MultipartFile file;
}
