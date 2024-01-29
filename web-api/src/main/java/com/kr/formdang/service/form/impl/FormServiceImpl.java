package com.kr.formdang.service.form.impl;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.enums.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormTbRepository formTbRepository;
    private final QuestionTbRepository questionTbRepository;
    private final FormSubTbRepository formSubTbRepository;
    private final AdminSubTbRepository adminSubTbRepository;
    private final FormMapper formMapper;

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

        HashMap<String, Object> params = new HashMap<>();
        params.put("offset", pageRequest.getOffset());
        params.put("pageSize", pageRequest.getPageSize());
        params.put("aid", formFindDto.getAid());

        if (formFindDto.isSurvey()) { // 설문 타입
            params.put("type", FormTypeEnum.INSPECTION_TYPE.getCode());
        }
        else if (formFindDto.isQuiz()) { // 퀴즈 타입
            params.put("type", FormTypeEnum.QUIZ_TYPE.getCode());
        }

        if (formFindDto.isProgressStatus()) { // 진행중
            params.put("end_flag", FormEndFlagEnum.PROGRESS.getCode());
            params.put("del_flag", FormDelFlagEnum.NOT_DEL.getCode());
            params.put("status", FormStatusEnum.NORMAL_STATUS.getCode());
        }  else if (formFindDto.isEndStatus()) { // 종료
            params.put("end_flag", FormEndFlagEnum.END.getCode());
            params.put("del_flag", FormDelFlagEnum.NOT_DEL.getCode());
        } else if (formFindDto.isTempStatus()) { // 임시저장
            params.put("end_flag", FormEndFlagEnum.PROGRESS.getCode());
            params.put("del_flag", FormDelFlagEnum.NOT_DEL.getCode());
            params.put("status", FormStatusEnum.TEMP_STATUS.getCode());
        } else if (formFindDto.isDelStatus()) { // 삭제
            params.put("del_flag", FormDelFlagEnum.DEL.getCode());
        }

        if (formFindDto.isRecent()) { // 최신순
            params.put("order", FormOrderEnum.RECENT.getCode());
        } else if (formFindDto.isLast()) { // 마감 순
            params.put("order", FormOrderEnum.LAST.getCode());
        }

        return new PageImpl(formMapper.findForms(params), pageRequest, formMapper.findFormsCnt(params));
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
