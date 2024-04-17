package com.kr.formdang.service.admin;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.entity.FileUploadFailTbEntity;
import com.kr.formdang.exception.FormException;
import com.kr.formdang.constant.ResultCode;
import com.kr.formdang.repository.AdminSubTbRepository;
import com.kr.formdang.repository.AdminTbRepository;
import com.kr.formdang.repository.FileUploadFailTbRepository;
import com.kr.formdang.utils.file.FileUtils;
import com.kr.formdang.utils.file.dto.S3File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${formdang.url.fail-login}")
    private String formdang_fail_login;

    @Value("${formdang.url.success-login}")
    private String formdang_success_login;

    @Value("${formdang.url.success-paper-login}")
    private String formdang_success_paper_login;

    private final AdminTbRepository adminTbRepository;

    private final AdminSubTbRepository adminSubTbRepository;

    private final FileUploadFailTbRepository fileUploadFailTbRepository;

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
    public String successLogin(String accessToken, String refreshToken)  {

        log.info("■ 4. 폼당폼당 로그인 토큰 생성");

        Map<String, Object> params = new HashMap<>();
        params.put("accessToken", accessToken);
        params.put("refreshToken", refreshToken);

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        log.info("■ 5. 폼당폼당 로그인 URL 생성");
        return formdang_success_login
                + "?"
                + paramStr; // 폼당폼당 관리자 메인 페이지 URL
    }

    @Override
    public String successPaperLogin(String accessToken, String refreshToken) {
        log.info("■ 4. 폼당폼당 로그인 토큰 생성");

        Map<String, Object> params = new HashMap<>();
        params.put("accessToken", accessToken);
        params.put("refreshToken", refreshToken);

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        log.info("■ 5. 폼당폼당 로그인 URL 생성");
        return formdang_success_paper_login
                + "?"
                + paramStr; // 폼당폼당 관리자 메인 페이지 URL
    }

    @Override
    public String failLogin(Exception e) {
        return formdang_fail_login + "?fail=true";
    }

    @Override
    @Transactional
    public boolean updateProfile(Long aid, S3File profile, MultipartFile file) {
        if (profile == null) {
            log.error("■ 이미지 업로드 실패 확인 필요");
            fileUploadFailTbRepository.save(FileUploadFailTbEntity.builder()
                    .oriName(file.getOriginalFilename())
                    .ext(FileUtils.getFileExtension(file.getOriginalFilename()))
                    .size(String.valueOf(file.getSize()))
                    .build());
            return false;
        } else {
            log.info("■ 2. 프로필 업데이트 쿼리 시작");
            return adminTbRepository.updateProfile(aid, profile.getPath()) > 0 ? true : false;
        }
    }

}
