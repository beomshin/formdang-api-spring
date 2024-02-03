package com.kr.formdang.model.net.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.model.root.DefaultResponse;
import lombok.*;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FindFormListResponse extends DefaultResponse {

    private List list;
    private Long totalElements;
    private Integer totalPage;
    private Integer curPage;
    private Analyze analyze;

    public FindFormListResponse(Page<FormTbEntity> pages, AdminSubTbEntity adminSubTb) {
        this.list = pages.getContent().stream().map(Forms::new).collect(Collectors.toList());
        this.totalElements = pages.getTotalElements();
        this.totalPage = pages.getTotalPages();
        this.curPage = pages.getNumber();
        this.analyze = new Analyze(adminSubTb);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class Forms {
        private long fid;
        private Integer type;
        private String title;
        private String logoUrl;
        private Integer status;
        private Integer endFlag;
        private Integer delFlag;

        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "Asia/Seoul")
        private Timestamp regDt;

        public Forms(FormTbEntity formTbEntity) {
            this.fid = formTbEntity.getFid();
            this.type = formTbEntity.getFormType();
            this.title = formTbEntity.getTitle();
            this.logoUrl = formTbEntity.getLogoUrl();
            this.status = formTbEntity.getStatus();
            this.endFlag = formTbEntity.getEndFlag();
            this.delFlag = formTbEntity.getDelFlag();
            this.regDt = formTbEntity.getRegDt();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class Analyze {
        private Integer inspectionCnt;
        private Integer quizCnt;
        private Integer inspectionRespondentCnt;
        private Integer quizRespondentCnt;

        public Analyze(AdminSubTbEntity adminSubTbEntity) {
            this.inspectionCnt = adminSubTbEntity.getInspectionCnt();
            this.quizCnt = adminSubTbEntity.getQuizCnt();
            this.inspectionRespondentCnt = adminSubTbEntity.getInspectionRespondentCnt();
            this.quizRespondentCnt = adminSubTbEntity.getQuizRespondent_cnt();
        }

    }
}
