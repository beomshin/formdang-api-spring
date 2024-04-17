package com.kr.formdang.service.form;

import com.kr.formdang.dto.FormTbDto;
import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.common.exception.FormException;
import com.kr.formdang.layer.FormDataDto;
import com.kr.formdang.layer.FormFindDto;
import com.kr.formdang.layer.QuestionDataDto;
import com.kr.formdang.service.file.dto.S3File;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 폼 서비스
 */
public interface FormService {
    FormTbEntity submitForm(FormTbEntity formTbEntity, List<QuestionTbEntity> questionTbEntities); // 폼 생성 서비스
    Page<FormTbDto> findFormList(FormFindDto formFindDto); // 폼 리스트 조회 서비스
    AdminSubTbEntity analyzeForm(Long aid); // 종합분석 조회 서비스
    FormTbEntity findForm(Long aid, Long fid) throws FormException;
    List<QuestionTbEntity> findQuestions(Long fid) throws FormException;
    void updateForm(FormDataDto formDataDto, List<QuestionDataDto> questionDataDtos) throws FormException;
    void updateImage(Long fid, List<S3File> formFiles);
    FormTbEntity findPaper(FormDataDto formDataDto) throws FormException;
}
