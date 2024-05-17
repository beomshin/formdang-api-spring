package com.kr.formdang.service.admin.impl;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.FormException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.repository.AdminSubTbRepository;
import com.kr.formdang.repository.AdminTbRepository;
import com.kr.formdang.model.file.S3File;
import com.kr.formdang.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminTbRepository adminTbRepository;

    private final AdminSubTbRepository adminSubTbRepository;

    @Override
    @Transactional
    public AdminTbEntity saveSnsAdmin(AdminTbEntity adminTbEntity) throws FormException {
        log.info("■ 폼당폼당 어드민 테이블 조회");
        Optional<AdminTbEntity> adminTb = adminTbRepository.findBySubId(adminTbEntity.getSubId());
        if (adminTb.isPresent()) {
            log.info("■ 가입 유저 [aid: {}, id: {}, name: {}, subId: {}]", adminTb.get().getAid(), adminTb.get().getId(), adminTb.get().getName(), adminTb.get().getSubId());
            return adminTb.get();
        }

        try {
            if (adminTbEntity == null) throw new FormException(ResultCode.FAIL_SAVE_ADMIN);
            log.info("■ 폼당폼당 어드민 테이블 둥록");
            AdminTbEntity admin = adminTbRepository.save(adminTbEntity);
            log.info("■ 폼당폼당 어드민 서브 테이블 둥록");
            adminSubTbRepository.save(AdminSubTbEntity.builder().aid(admin.getAid()).build());
            return admin;
        } catch (FormException e) {
            log.error("■ 어드민 계정 정보 누락 오류 [{}]", e.getCode());
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("■ 어드민 계정 유니크 오류");
            throw new FormException(ResultCode.NOT_UNIQUE_ADMIN);
        } catch (Exception e) {
            log.error("■ 어드민 계정 저장 실패");
            throw new FormException(ResultCode.FAIL_SAVE_ADMIN);
        }
    }


    @Override
    public void updateProfile(Long aid, S3File profile, MultipartFile file) {
        adminTbRepository.updateProfile(aid, profile.getPath());
    }

}
