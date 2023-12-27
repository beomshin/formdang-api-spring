package com.kr.formdang.service.form.impl;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.enums.PageEnum;
import com.kr.formdang.mapper.FormMapper;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.layer.FormDataDto;
import com.kr.formdang.model.layer.FormFindDto;
import com.kr.formdang.model.layer.QuestionDataDto;
import com.kr.formdang.repository.AdminSubTbRepository;
import com.kr.formdang.repository.FormSubTbRepository;
import com.kr.formdang.repository.FormTbRepository;
import com.kr.formdang.repository.QuestionTbRepository;
import com.kr.formdang.service.form.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormTbRepository formTbRepository;
    private final QuestionTbRepository questionTbRepository;
    private final FormSubTbRepository formSubTbRepository;
    private final AdminSubTbRepository adminSubTbRepository;

    @Override
    @Transactional
    public FormTbEntity submitForm(FormTbEntity formTbEntity, List<QuestionTbEntity> questionTbEntities) {
        try {
            FormTbEntity formTb = formTbRepository.save(formTbEntity);
            questionTbEntities.stream().forEach(it -> it.setFid(formTb.getFid()));
            questionTbRepository.saveAll(questionTbEntities);
            FormSubTbEntity formSubTb = new FormSubTbEntity();
            formSubTb.setFid(formTb.getFid());
            formSubTbRepository.save(formSubTb);
            if (formTbEntity.getFormType() == 0) {
                adminSubTbRepository.countUpInspectionCnt(formTbEntity.getAid());
            } else if (formTbEntity.getFormType() == 1) {
                adminSubTbRepository.countUpQuizCnt(formTbEntity.getAid());
            }
            return formTb;
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        }
    }

    @Override
    public Page findForm(FormFindDto formFindDto) {
        if (formFindDto.isAllType()) {
            return formTbRepository.findByAidOrderByRegDtDesc(formFindDto.getAid(), PageRequest.of(formFindDto.getPage(), PageEnum.PAGE_12.getNum()));
        }else {
            return formTbRepository.findByFormTypeAndAidOrderByRegDtDesc(formFindDto.getType(), formFindDto.getAid(), PageRequest.of(formFindDto.getPage(), PageEnum.PAGE_12.getNum()));
        }
    }

    @Override
    public AdminSubTbEntity analyzeForm(Long aid) {
        return adminSubTbRepository.findByAid(aid);
    }
}
