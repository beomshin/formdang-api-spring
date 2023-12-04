package com.kr.formdang.service.impl;

import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.CustomException;
import com.kr.formdang.model.common.GlobalCode;
import com.kr.formdang.repository.AdminTbRepository;
import com.kr.formdang.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminTbRepository adminTbRepository;
    @Override
    public void saveAdmin(AdminTbEntity adminTbEntity) throws CustomException {
        try {
            if (adminTbEntity == null) throw new CustomException(GlobalCode.FAIL_SAVE_ADMIN);
            adminTbRepository.save(adminTbEntity);
        } catch (CustomException e) {
            log.error("[어드민 계정 정보 누락 오류] =========> ");
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("[어드민 계정 유니크 오류] =========> ");
            throw new CustomException(GlobalCode.NOT_UNIQUE_ADMIN);
        } catch (Exception e) {
            log.error("[어드민 계정 저장 실패] =========> ");
            throw new CustomException(GlobalCode.FAIL_SAVE_ADMIN);
        }
    }
}
