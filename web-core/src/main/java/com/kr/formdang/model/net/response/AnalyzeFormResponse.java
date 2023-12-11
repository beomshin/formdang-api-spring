package com.kr.formdang.model.net.response;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.model.root.DefaultResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)

@NoArgsConstructor
public class AnalyzeFormResponse extends DefaultResponse {

    private Integer inspectionCnt;
    private Integer quizCnt;
    private Integer inspectionRespondentCnt;
    private Integer quizRespondentCnt;

    public AnalyzeFormResponse(AdminSubTbEntity adminSubTbEntity) {
        this.inspectionCnt = adminSubTbEntity.getInspectionCnt();
        this.quizCnt = adminSubTbEntity.getQuizCnt();
        this.inspectionRespondentCnt = adminSubTbEntity.getInspectionRespondentCnt();
        this.quizRespondentCnt = adminSubTbEntity.getQuizRespondent_cnt();
    }


}
