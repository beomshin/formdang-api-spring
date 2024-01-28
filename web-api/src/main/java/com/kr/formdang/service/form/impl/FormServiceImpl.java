package com.kr.formdang.service.form.impl;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.enums.FormStatusEnum;
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

    /**
     * 폼 저장
     *
     * - 폼 저장
     * - 질문 리스트 저장
     * - 유저 생성 개수 업데이트
     *
     * @param formTbEntity
     * @param questionTbEntities
     * @return
     */
    @Override
    @Transactional
    public FormTbEntity submitForm(FormTbEntity formTbEntity, List<QuestionTbEntity> questionTbEntities) {
        try {
            FormTbEntity formTb = formTbRepository.save(formTbEntity); // 폼 생성

            questionTbEntities.stream().forEach(it -> it.setFid(formTb.getFid()));
            questionTbRepository.saveAll(questionTbEntities); // 질문 리스트 생성

            FormSubTbEntity formSubTb = new FormSubTbEntity();
            formSubTb.setFid(formTb.getFid());
            formSubTbRepository.save(formSubTb); // 폼 서브 저장 테이블 생성

            if (formTbEntity.getFormType() == 0) {
                adminSubTbRepository.countUpInspectionCnt(formTbEntity.getAid()); // 설문 타입 생성 개수 증가
            } else if (formTbEntity.getFormType() == 1) {
                adminSubTbRepository.countUpQuizCnt(formTbEntity.getAid()); // 퀴즈 타입 생성 개수 증가
            }

            return formTb;
        } catch (Exception e) {
            log.error("{}", e);
            return null;
        }
    }

    /**
     * 폼 리스트 조회
     * 페이징 12
     *
     * 상태 처리 - 전체, 진행, 종료, 임시
     *
     * @param formFindDto
     * @return
     */
    @Override
    public Page findForm(FormFindDto formFindDto) {
        PageRequest pageRequest = PageRequest.of(formFindDto.getPage(), PageEnum.PAGE_12.getNum());
        if (formFindDto.isAllStatus()) { // 전체 조회
            return formTbRepository.findByAidOrderByRegDtDesc(formFindDto.getAid(), pageRequest);
        } else if (formFindDto.isProgressStatus()) { // 진행중인 폼 리스트 조회
            return formTbRepository.findByAidAndStatusAndEndFlagOrderByRegDtDesc(formFindDto.getAid(), FormStatusEnum.NORMAL_STATUS.getCode(), FormStatusEnum.NOT_END_STATUS.getCode(), pageRequest);
        } else if (formFindDto.isEndStatus()) { // 종료 폼 리스트 조회
            return formTbRepository.findByAidAndEndFlagOrderByRegDtDesc(formFindDto.getAid(), FormStatusEnum.END_STATUS.getCode(), pageRequest);
        } else if (formFindDto.isTempStatus()) { // 임시 폼 리스트 조회
            return formTbRepository.findByAidAndStatusAndEndFlagOrderByRegDtDesc(formFindDto.getAid(), FormStatusEnum.TEMP_STATUS.getCode(), FormStatusEnum.NOT_END_STATUS.getCode(), pageRequest);
        } else { // 예외시 기본 조회
            return formTbRepository.findByAidOrderByRegDtDesc(formFindDto.getAid(), pageRequest);
        }
    }

    /**
     * 폼 서브 정보 테이블 조회
     * @param aid
     * @return
     */
    @Override
    public AdminSubTbEntity analyzeForm(Long aid) {
        return adminSubTbRepository.findByAid(aid);
    }
}
