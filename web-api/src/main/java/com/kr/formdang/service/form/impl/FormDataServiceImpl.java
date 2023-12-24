package com.kr.formdang.service.form.impl;

import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.model.layer.FormDataDto;
import com.kr.formdang.model.layer.QuestionDataDto;
import com.kr.formdang.service.form.FormDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

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
                .questionDetail(Arrays.stream(questionDataDto.getDetail()).collect(Collectors.joining("|")))
                .count(questionDataDto.getCount())
                .quizAnswer(Arrays.stream(questionDataDto.getAnswer()).collect(Collectors.joining("|")))
                .imageUrl(questionDataDto.getImageUrl())
                .build();
    }
}
