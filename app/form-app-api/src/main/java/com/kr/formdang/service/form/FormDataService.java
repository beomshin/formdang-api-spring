package com.kr.formdang.service.form;

import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.layer.FormDataDto;
import com.kr.formdang.layer.QuestionDataDto;

/**
 * 엔티티 생성 서비스
 */
public interface FormDataService {
    FormTbEntity getFormData(FormDataDto formDataDto); // 폼 엔티티 생성
    QuestionTbEntity getQuestionData(QuestionDataDto questionDataDto); // 질문 엔티티 생성
}
