package com.kr.formdang.service.form.impl;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.FormSubTbEntity;
import com.kr.formdang.entity.FormTbEntity;
import com.kr.formdang.entity.QuestionTbEntity;
import com.kr.formdang.enums.*;
import com.kr.formdang.mapper.FormMapper;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.model.dao.form.FindFormDto;
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
            log.info("■ 2. 폼 테이블 등록");
            FormTbEntity formTb = formTbRepository.save(formTbEntity); // 폼 생성

            log.info("■ 2. 질문 리스트 테이블 등록");
            questionTbEntities.stream().forEach(it -> it.setFid(formTb.getFid()));
            questionTbRepository.saveAll(questionTbEntities); // 질문 리스트 생성

            log.info("■ 3. 폼 서브 테이블 등록");
            FormSubTbEntity formSubTb = new FormSubTbEntity();
            formSubTb.setFid(formTb.getFid());
            formSubTbRepository.save(formSubTb); // 폼 서브 저장 테이블 생성

            if (formTbEntity.getFormType() == 0) {
                log.info("■ 4. 어드민 서브 테이블 설문 타입 개수 증가");
                adminSubTbRepository.countUpInspectionCnt(formTbEntity.getAid()); // 설문 타입 생성 개수 증가
            } else if (formTbEntity.getFormType() == 1) {
                log.info("■ 4. 어드민 서브 테이블 퀴즈 타입 개수 증가");
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
     * 타입 처리 - 전체, 설문, 퀴즈
     * 상태 처리 - 전체, 진행, 종료, 임시
     * 정렬 처리 - 최순, 답변, 마감
     *
     * @param formFindDto
     * @return
     */
    @Override
    public Page findForm(FormFindDto formFindDto) {
        PageRequest pageRequest = PageRequest.of(formFindDto.getPage(), PageEnum.PAGE_12.getNum());
        FindFormDto params = FindFormDto.builder() // 조회 파라미터 생성
                .aid(formFindDto.getAid())
                .offset(pageRequest.getOffset())
                .pageSize(pageRequest.getPageSize())
                .type(formFindDto.getType())
                .delFlag(formFindDto.getDelFlag())
                .endFlag(formFindDto.getEndFlag())
                .status(formFindDto.getStatus())
                .order(formFindDto.getOrder())
                .build();

        log.info("■ 2. 폼 리스트 조회 쿼리 시작");
        return new PageImpl(formMapper.findForms(params), pageRequest, formMapper.findFormsCnt(params));
    }

    /**
     * 폼 서브 정보 테이블 조회
     * @param aid
     * @return
     */
    @Override
    public AdminSubTbEntity analyzeForm(Long aid) {
        log.info("■ 3. 종합 분석 정보 조회 쿼리 시작");
        return adminSubTbRepository.findByAid(aid);
    }
}
