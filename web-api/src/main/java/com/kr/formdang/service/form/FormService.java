package com.kr.formdang.service.form;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.layer.FormDataDto;
import com.kr.formdang.model.layer.FormFindDto;
import com.kr.formdang.model.layer.QuestionDataDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FormService {

    FormTbEntity submitForm(FormTbEntity formTbEntity, List<QuestionTbEntity> questionTbEntities);
    Page findForm(FormFindDto formFindDto);
    AdminSubTbEntity analyzeForm(Long aid);

}
