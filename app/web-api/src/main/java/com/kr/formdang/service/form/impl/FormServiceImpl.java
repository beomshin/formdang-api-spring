package com.kr.formdang.service.form.impl;

import com.kr.formdang.entity.*;

import com.kr.formdang.exception.FormException;
import com.kr.formdang.mapper.FormMapper;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.model.SqlFormParam;
import com.kr.formdang.prop.PaperProp;
import com.kr.formdang.repository.*;
import com.kr.formdang.model.file.S3File;
import com.kr.formdang.service.form.FormService;
import com.kr.formdang.utils.ClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Page<FormTbEntity> findFormList(SqlFormParam sqlFormParam, PageRequest pageRequest) {
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
    public void modifyForm(FormTbEntity modifyForm, List<QuestionTbEntity> modifyQuestions) throws FormException {

        log.info("■ 폼 상세 정보 조회 쿼리 시작");
        FormTbEntity formTb = formTbRepository.findByAidAndFid(modifyForm.getAid(), modifyForm.getFid())
                .orElseThrow(() -> new FormException(ResultCode.NOT_FIND_FORM));

        if (formTb.isUserSubmitForm()) throw new FormException(ResultCode.REFUSE_ALREADY_START_FORM);
        else if (formTb.isDeleteForm()) throw new FormException(ResultCode.REFUSE_ALREADY_DELETE_FORM);
        else if (formTb.isEndForm()) throw new FormException(ResultCode.REFUSE_ALREADY_END_FORM);

        List<QuestionTbEntity> questionTbs = questionTbRepository.findByFidOrderByOrderAsc(formTb.getFid());

        log.info("■ 폼 수정 데이터 엔티티 적용");
        formTb.modify(modifyForm); // 변경감지를 통한 업데이트 기존 정보와 동일하면 업데이트 안함

        log.info("■ 폼 질문 수정 데이터 엔티티 적용");
        int question_cnt, modify_cnt; // 변경 질문 개수, 가변 질문 개수

        boolean isIncrease = modifyQuestions.size() > questionTbs.size(); // 질문의 개수가 증가된 케이스
        boolean isDecrease = modifyQuestions.size() < questionTbs.size(); // 질문의 개수가 감소된 케이스

        if (isIncrease) {

            question_cnt = questionTbs.size();
            modify_cnt = modifyQuestions.size();

            List<QuestionTbEntity> insertEntities = new ArrayList<>();
            for (int i=question_cnt; i < modify_cnt; i++) {
                modifyQuestions.get(i).setFid(formTb.getFid());
                insertEntities.add(modifyQuestions.get(i));
            }

            log.info("■ 폼 추가 질문 등록 쿼리 시작");
            questionTbRepository.saveAll(insertEntities);

        } else if (isDecrease) {

            question_cnt = modifyQuestions.size();
            modify_cnt = questionTbs.size();

            List<QuestionTbEntity> deleteEntities = new ArrayList<>();
            for (int i=question_cnt; i< modify_cnt; i++) {
                deleteEntities.add(questionTbs.get(i));
            }

            log.info("■ 폼 등록 질문 삭제 쿼리 시작");
            questionTbRepository.deleteAll(deleteEntities);

        } else { // 질문 개수 동일 케이스
            question_cnt = modifyQuestions.size();
        }

        for(int i=0; i < question_cnt; i++) {
            questionTbs.get(i).modify(modifyQuestions.get(i));
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
        FormTbEntity queryParam = FormTbEntity.builder().fid(formDataDto.getFid()).build();
        Optional<FormSubTbEntity> formSubTb = formSubTbRepository.findByFormTbAndFormUrlKey(queryParam, key);
        if (!formSubTb.isPresent()) throw new FormException(ResultCode.NOT_FIND_FORM);

        FormTbEntity formTb = formSubTb.get().getFormTb();

        if (formTb.getAid() == formDataDto.getAid()) {
            log.debug("■ 폼 작성자 접근");
            return formTb;
        } else if (formTb.isDeleteForm()) {
            throw new FormException(ResultCode.DELETE_FORM);
        } else if (formTb.isEndForm()) {
            throw new FormException(ResultCode.END_FORM);
        } else if (!formTb.isStartForm()) {
            throw new FormException(ResultCode.NOT_START_FORM);
        } else if (formTb.isExceedSubject()) {
            throw new FormException(ResultCode.IS_MAX_RESPONSE);
        } else if (!(formTb.isSubmitRangeDt())) {
            throw new FormException(ResultCode.IS_NOT_RIGHT_DATE);
        }

        log.info("■ 제출 여부 확인 조회 쿼리 시작");
        int isSubmit = answerTbRepository.countByFidAndAid(formTb.getFid(), formDataDto.getAid());
        if (isSubmit > 0) throw new FormException(ResultCode.IS_SUBMIT);

        log.info("■ 그룹 폼 조회 쿼리 시작");
        List<GroupFormTbEntity> groupFormTbEntity = groupFormTbRepository.findByFid(formTb.getFid()); // 해당 폼 그룹 리스트 조회

        if (!groupFormTbEntity.isEmpty()) {

            List<GroupTbEntity> groups =  groupFormTbEntity.stream().map(it -> GroupTbEntity.builder().gid(it.getGid()).build()).collect(Collectors.toList());

            log.info("■ 그룹 권한 유저 조회 쿼리 시작");
            List<GroupMemberTbEntity> groupMemberTbEntity = groupMemberTbRepository.findByAidAndGroupTbIn(formDataDto.getAid(), groups); // 그룹 리스트 중 포함된 유저 여부 확인

            if (groupMemberTbEntity.isEmpty()) {
                throw new FormException(ResultCode.IS_NOT_GROUP_FORM_USER); // 그룹 폼 권한 미유저
            }

            log.info("■ 그룹 폼 권한 검사 통과");
        }

        return formTb;
    }

}
