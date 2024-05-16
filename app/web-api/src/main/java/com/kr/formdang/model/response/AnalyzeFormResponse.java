package com.kr.formdang.model.response;

import com.kr.formdang.model.AbstractResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnalyzeFormResponse extends AbstractResponse {

    private Integer inspectionCnt;
    private Integer quizCnt;
    private Integer inspectionRespondentCnt;
    private Integer quizRespondentCnt;


}
