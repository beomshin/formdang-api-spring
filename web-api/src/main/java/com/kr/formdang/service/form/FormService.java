package com.kr.formdang.service.form;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.model.layer.FormFindDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 폼 서비스
 */
public interface FormService {
    FormTbEntity submitForm(FormTbEntity formTbEntity, List<QuestionTbEntity> questionTbEntities); // 폼 생성 서비스
    Page findForm(FormFindDto formFindDto); // 폼 리스트 조회 서비스
    AdminSubTbEntity analyzeForm(Long aid); // 종합분석 조회 서비스

}
