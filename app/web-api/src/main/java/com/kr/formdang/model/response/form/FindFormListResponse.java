package com.kr.formdang.model.response.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kr.formdang.model.response.AbstractResponse;
import lombok.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindFormListResponse extends AbstractResponse {

    private List list;
    private Long totalElements;
    private Integer totalPage;
    private Integer curPage;
    private Analyze analyze;

    @Getter
    @Setter
    @ToString(callSuper = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
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
    @ToString(callSuper = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Analyze {
        private Integer inspectionCnt;
        private Integer quizCnt;
        private Integer inspectionRespondentCnt;
        private Integer quizRespondentCnt;

    }
}
