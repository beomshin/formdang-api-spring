package com.kr.formdang.net.req;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileListRequest {

    @NotNull(message = "파일이 누락되었습니다.")
    private List<MultipartFile> files;

    @NotNull(message = "순번이 누락되었습니다.")
    private List<Integer> orders; // 질문 순번

    @NotNull(message = "타입이 누락되었습니다.")
    private List<Integer> types; // 타입 (0: 로고, 1: 질문)

}
