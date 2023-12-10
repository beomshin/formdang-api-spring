package com.kr.formdang.service.form;

import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.model.layer.FormDataDto;
import com.kr.formdang.model.layer.QuestionDataDto;

public interface FormDataService {

    FormTbEntity getFormData(FormDataDto formDataDto);

    QuestionTbEntity getQuestionData(QuestionDataDto questionDataDto);
}
