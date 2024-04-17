package com.kr.formdang.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kr.formdang.dto.DefaultResponse;
import lombok.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindFormListResponse extends DefaultResponse {

    private List list;
    private Long totalElements;
    private Integer totalPage;
    private Integer curPage;
    private Analyze analyze;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Forms {
        private long fid;
        private Integer type;
        private String title;
        private String logoUrl;
        private Integer status;
        private Integer endFlag;
        private Integer delFlag;
        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "Asia/Seoul")
        private Timestamp regDt;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Analyze {
        private Integer inspectionCnt;
        private Integer quizCnt;
        private Integer inspectionRespondentCnt;
        private Integer quizRespondentCnt;

    }
}
