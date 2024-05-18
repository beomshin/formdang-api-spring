package com.kr.formdang.service.form;

import com.kr.formdang.entity.AnswerTbEntity;
import com.kr.formdang.model.*;
import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.exception.FormException;
import com.kr.formdang.model.file.S3File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 폼 서비스
 */
public interface FormService {
    FormTbEntity submitForm(FormTbEntity formTbEntity, List<QuestionTbEntity> questionTbEntities); // 폼 생성 서비스
    Page<FormTbEntity> findFormList(SqlFormParam sqlFormParam, PageRequest pageRequest); // 폼 리스트 조회 서비스
    AdminSubTbEntity analyzeForm(Long aid); // 종합분석 조회 서비스
    FormTbEntity findForm(Long aid, Long fid) throws FormException;
    List<QuestionTbEntity> findQuestions(Long fid) throws FormException;
    void modifyForm(FormTbEntity modifyForm, List<QuestionTbEntity> modifyQuestions) throws FormException;
    void updateImage(Long fid, S3File file);
    FormTbEntity findPaper(FormTbEntity formDataDto, String key) throws FormException;
    List<AnswerTbEntity> findAnswers(FormTbEntity formTb) throws FormException;
}
