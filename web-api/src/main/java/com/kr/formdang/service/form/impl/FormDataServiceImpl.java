package com.kr.formdang.service.form.impl;

import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.model.layer.FormDataDto;
import com.kr.formdang.model.layer.QuestionDataDto;
import com.kr.formdang.service.form.FormDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormDataServiceImpl implements FormDataService {
    @Override
    public FormTbEntity getFormData(FormDataDto formDataDto) {
        return FormTbEntity.builder()
                .aid(formDataDto.getAid())
                .title(formDataDto.getTitle())
                .formType(formDataDto.getType())
                .formDetail(formDataDto.getDetail())
                .beginDt(formDataDto.getBeginDt())
                .endDt(formDataDto.getEndDt())
                .maxRespondent(formDataDto.getMaxRespondent())
                .questionCount(formDataDto.getQuestionCount())
                .logoUrl(formDataDto.getLogoUrl())
                .themaUrl(formDataDto.getThemaUrl())
                .build();
    }

    @Override
    public QuestionTbEntity getQuestionData(QuestionDataDto questionDataDto) {
        return QuestionTbEntity.builder()
                .order(questionDataDto.getOrder())
                .questionType(questionDataDto.getType())
                .title(questionDataDto.getTitle())
                .questionPlaceholder(questionDataDto.getPlaceholder())
                .questionDetail(questionDataDto.getDetail())
                .count(questionDataDto.getCount())
                .quizAnswer(questionDataDto.getAnswer())
                .imageUrl(questionDataDto.getImageUrl())
                .build();
    }
}
