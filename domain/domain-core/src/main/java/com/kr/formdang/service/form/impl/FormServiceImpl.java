package com.kr.formdang.service.form.impl;

import com.kr.formdang.dto.FormTbDto;
import com.kr.formdang.entity.*;

import com.kr.formdang.exception.FormException;
import com.kr.formdang.mapper.FormMapper;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.dto.SqlFormParam;
import com.kr.formdang.prop.PaperProp;
import com.kr.formdang.repository.*;
import com.kr.formdang.dto.S3File;
import com.kr.formdang.service.form.FormService;
import com.kr.formdang.utils.ClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormTbRepository formTbRepository;
    private final QuestionTbRepository questionTbRepository;
    private final FormSubTbRepository formSubTbRepository;
    private final AdminSubTbRepository adminSubTbRepository;
    private final FormMapper formMapper;
    private final GroupFormTbRepository groupFormTbRepository;
    private final GroupMemberTbRepository groupMemberTbRepository;
    private final AnswerTbRepository answerTbRepository;
    private final PaperProp paperProp;

    /**
     * 폼 저장
     * - 폼 저장
     * - 질문 리스트 저장
     * - 유저 생성 개수 업데이트
     */
    @Override
    @Transactional
    public FormTbEntity submitForm(FormTbEntity formTbEntity, List<QuestionTbEntity> questionTbEntities) {
        log.info("■ 폼 테이블 등록 쿼리 시작");
        FormTbEntity formTb = formTbRepository.save(formTbEntity); // 폼 생성

        log.info("■ 질문 리스트 테이블 등록 쿼리 시작");
        questionTbEntities.forEach(it -> it.setFid(formTb.getFid()));
        questionTbRepository.saveAll(questionTbEntities); // 질문 리스트 생성

        String key = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", formTb.getFormType());
        params.put("fid", formTb.getFid());
        params.put("key", key);

        String url = paperProp.getHost() + paperProp.getPaper() + "?" + ClientUtils.convertMapToParam(params);

        log.info("■ 폼 서브 테이블 등록 쿼리 시작");
        FormSubTbEntity formSubTb = FormSubTbEntity.builder()
                .formTb(formTb)
                .formUrlKey(key)
                .formUrl(url)
                .build();
        formSubTbRepository.save(formSubTb); // 폼 서브 저장 테이블 생성

        if (formTbEntity.getFormType() == 0) {
            log.info("■ 어드민 서브 테이블 설문 타입 개수 증가 쿼리 시작");
            adminSubTbRepository.countUpInspectionCnt(formTbEntity.getAid()); // 설문 타입 생성 개수 증가
        } else if (formTbEntity.getFormType() == 1) {
            log.info("■ 어드민 서브 테이블 퀴즈 타입 개수 증가 쿼리 시작");
            adminSubTbRepository.countUpQuizCnt(formTbEntity.getAid()); // 퀴즈 타입 생성 개수 증가
        }

        return formTb;
    }

    /**
     * 폼 리스트 조회
     * 페이징 12
     *
     * 타입 처리 - 전체, 설문, 퀴즈
     * 상태 처리 - 전체, 진행, 종료, 임시
     * 정렬 처리 - 최순, 답변, 마감
     */
    @Override
    public Page<FormTbDto> findFormList(SqlFormParam sqlFormParam, PageRequest pageRequest) {
        return new PageImpl<>(formMapper.findForms(sqlFormParam), pageRequest, formMapper.findFormsCnt(sqlFormParam));
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

    @Override
    public FormTbEntity findForm(Long aid, Long fid) throws FormException {
        Optional<FormTbEntity> formTb = formTbRepository.findByAidAndFid(aid, fid);
        if (formTb.isPresent()) {
            return formTb.get();
        } else {
            throw new FormException(ResultCode.NOT_FIND_FORM);
        }
    }

    @Override
    public List<QuestionTbEntity> findQuestions(Long fid) throws FormException {
        List<QuestionTbEntity> questionTbEntities = questionTbRepository.findByFid(fid);
        if (questionTbEntities != null && !questionTbEntities.isEmpty()) {
            return questionTbEntities;
        } else {
            throw new FormException(ResultCode.NOT_FIND_QUESTIONS);
        }
    }

    @Override
    @Transactional
    public void updateForm(FormTbEntity formDataDto, List<QuestionTbEntity> questionDataDtos) throws FormException {
        log.info("■ 폼 상세 정보 조회 쿼리 시작");
        Optional<FormTbEntity> formTb = formTbRepository.findByAidAndFid(formDataDto.getAid(), formDataDto.getFid());
        if (!formTb.isPresent()) throw new FormException(ResultCode.NOT_FIND_FORM);
        else if (formTb.get().getAnswerCount() > 0) throw new FormException(ResultCode.REFUSE_ALREADY_START_FORM);
        else if (formTb.get().getDelFlag() == 1) throw new FormException(ResultCode.REFUSE_ALREADY_DELETE_FORM);
        else if (formTb.get().getEndFlag() == 1) throw new FormException(ResultCode.REFUSE_ALREADY_END_FORM);

        List<QuestionTbEntity> questionTbEntities = questionTbRepository.findByFidOrderByOrderAsc(formTb.get().getFid());
        log.info("■ 폼 수정 데이터 엔티티 적용");
        formTb.get().updateForm(formDataDto); // 변경감지를 통한 업데이트 기존 정보와 동일하면 업데이트 안함
        log.info("■ 폼 질문 수정 데이터 엔티티 적용");
        int question_cnt = 0, over_cnt = 0;
        if (questionDataDtos.size() == questionTbEntities.size()) {
            question_cnt = questionDataDtos.size();
        } else if (questionDataDtos.size() > questionTbEntities.size()) {
            question_cnt = questionTbEntities.size();
            over_cnt = questionDataDtos.size();
            List<QuestionTbEntity> insertEntities = new ArrayList<>();
            for (int i=question_cnt; i < over_cnt; i++) {
                questionDataDtos.get(i).setFid(formTb.get().getFid());
                insertEntities.add(questionDataDtos.get(i));
            }
            log.info("■ 폼 추가 질문 등록 쿼리 시작");
            questionTbRepository.saveAll(insertEntities);
        } else if (questionDataDtos.size() < questionTbEntities.size()) {
            question_cnt = questionDataDtos.size();
            over_cnt = questionTbEntities.size();
            List<QuestionTbEntity> deleteEntities = new ArrayList<>();
            for (int i=question_cnt; i< over_cnt; i++) {
                deleteEntities.add(questionTbEntities.get(i));
            }
            log.info("■ 폼 등록 질문 삭제 쿼리 시작");
            questionTbRepository.deleteAll(deleteEntities);
        }
        for(int i=0; i < question_cnt; i++) {
            questionTbEntities.get(i).updateQuestion(questionDataDtos.get(i));
        }
    }

    @Override
    public void updateImage(Long fid, S3File file) {
        if (file.isLogo()) {
            formTbRepository.updateLogoUrl(fid, file.getPath());
        } else if (file.isQuestion()) {
            questionTbRepository.updateImageUrl(fid, file.getPath(), file.getOrder());
        }
    }

    @Override
    @Transactional
    public FormTbEntity findPaper(FormTbEntity formDataDto, String key) throws FormException {
        log.info("■ 폼 상세 정보 조회 쿼리 시작");
        Optional<FormSubTbEntity> formSubTb = formSubTbRepository.findByFormTbAndFormUrlKey(FormTbEntity.builder().fid(formDataDto.getFid()).build(), key);
        if (formSubTb.isPresent()) {

            FormTbEntity formTb = formSubTb.get().getFormTb();
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

            if (Objects.equals(formTb.getAid(), formDataDto.getAid())) {
                log.info("■ 작성자 페이지 접근");
                return formTb;
            } else if (formTb.getDelFlag() == 1) {
                throw new FormException(ResultCode.DELETE_FORM);
            } else if (formTb.getEndFlag() == 1) {
                throw new FormException(ResultCode.END_FORM);
            } else if (formTb.getStatus() != 1) {
                throw new FormException(ResultCode.NOT_START_FORM);
            } else if (formTb.getMaxRespondent() !=  0 && formTb.getAnswerCount() >= formTb.getMaxRespondent()) {
                throw new FormException(ResultCode.IS_MAX_RESPONSE);
            } else if (!(now.compareTo(formTb.getBeginDt()) >= 0 && now.compareTo(formTb.getEndDt()) <= 0)) {
                throw new FormException(ResultCode.IS_NOT_RIGHT_DATE);
            }

            log.info("■ 제출 여부 확인 조회 쿼리 시작");
            int isSubmit = answerTbRepository.countByFidAndAid(formTb.getFid(), formDataDto.getAid());
            if (isSubmit > 0) {
                throw new FormException(ResultCode.IS_SUBMIT);
            }

            log.info("■ 그룹 폼 조회 쿼리 시작");
            List<GroupFormTbEntity> groupFormTbEntity = groupFormTbRepository.findByFid(formTb.getFid());

            if (!groupFormTbEntity.isEmpty()) {

                log.info("■ 그룹 권한 유저 조회 쿼리 시작");
                List<GroupMemberTbEntity> groupMemberTbEntity = groupMemberTbRepository.findByAidAndGroupTbIn(formDataDto.getAid(), groupFormTbEntity.stream().map(it -> new GroupTbEntity(it.getGid())).collect(Collectors.toList()));
                if (groupMemberTbEntity.isEmpty()) {
                    throw new FormException(ResultCode.IS_NOT_GROUP_FORM_USER); // 그룹 폼 권한 미유저
                }
                log.info("■ 그룹 폼 권한 검사 통과");
            }

            return formTb;
        } else {
            throw new FormException(ResultCode.NOT_FIND_FORM);
        }
    }

}
