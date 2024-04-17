package com.kr.formdang.dto.res;

import com.kr.formdang.dto.DefaultResponse;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyzeFormResponse extends DefaultResponse {

    private Integer inspectionCnt;
    private Integer quizCnt;
    private Integer inspectionRespondentCnt;
    private Integer quizRespondentCnt;


}
