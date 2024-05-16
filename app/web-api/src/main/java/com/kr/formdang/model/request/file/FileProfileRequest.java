package com.kr.formdang.model.request.file;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileProfileRequest {

    @NotNull
    private MultipartFile profile;
}
