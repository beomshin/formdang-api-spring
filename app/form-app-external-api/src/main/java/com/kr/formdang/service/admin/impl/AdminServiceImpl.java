package com.kr.formdang.service.admin.impl;

import com.kr.formdang.entity.AdminSubTbEntity;
import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.entity.FileUploadFailTbEntity;
import com.kr.formdang.exception.CustomException;
import com.kr.formdang.common.GlobalCode;
import com.kr.formdang.utils.file.dto.GlobalFile;
import com.kr.formdang.external.auth.JwtTokenResponse;
import com.kr.formdang.repository.AdminSubTbRepository;
import com.kr.formdang.repository.AdminTbRepository;
import com.kr.formdang.repository.FileUploadFailTbRepository;
import com.kr.formdang.service.admin.AdminService;
import com.kr.formdang.service.api.TokenService;
import com.kr.formdang.utils.file.FileUtils;
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

    private final AdminTbRepository adminTbRepository;
    private final AdminSubTbRepository adminSubTbRepository;
    private final TokenService tokenService;
    private final FileUploadFailTbRepository fileUploadFailTbRepository;

    @Value("${formdang.url.fail-login}")
    private String formdang_fail_login;

    @Value("${formdang.url.success-login}")
    private String formdang_success_login;

    @Value("${formdang.url.success-paper-login}")
    private String formdang_success_paper_login;

    @Value("${token.access-key}")
    private String accessKey;

    @Override
    @Transactional
    public AdminTbEntity saveSnsAdmin(AdminTbEntity adminTbEntity) throws CustomException {
        log.info("■ 폼당폼당 어드민 테이블 조회");
        Optional<AdminTbEntity> adminTb = adminTbRepository.findBySubId(adminTbEntity.getSubId());
        if (adminTb.isPresent()) {
            log.info("■ 가입 유저 [aid: {}, id: {}, name: {}, subId: {}]", adminTb.get().getAid(), adminTb.get().getId(), adminTb.get().getName(), adminTb.get().getSubId());
            return adminTb.get();
        }

        try {
            if (adminTbEntity == null) throw new CustomException(GlobalCode.FAIL_SAVE_ADMIN);
            log.info("■ 폼당폼당 어드민 테이블 둥록");
            AdminTbEntity admin = adminTbRepository.save(adminTbEntity);
            log.info("■ 폼당폼당 어드민 서브 테이블 둥록");
            adminSubTbRepository.save(AdminSubTbEntity.builder().aid(admin.getAid()).build());
            return admin;
        } catch (CustomException e) {
            log.error("■ 어드민 계정 정보 누락 오류 [{}]", e.getCode());
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("■ 어드민 계정 유니크 오류");
            throw new CustomException(GlobalCode.NOT_UNIQUE_ADMIN);
        } catch (Exception e) {
            log.error("■ 어드민 계정 저장 실패");
            throw new CustomException(GlobalCode.FAIL_SAVE_ADMIN);
        }
    }

    @Override
    public String successLogin(AdminTbEntity adminTb)  {
        log.info("■ 4. 폼당폼당 로그인 토큰 생성");
        JwtTokenResponse jwtTokenResponse = tokenService.getLoginToken(String.valueOf(adminTb.getAid()), adminTb.getName(), adminTb.getProfile(), accessKey); // 폼당폼당 JWT 토큰 요청
        Map<String, Object> params = new HashMap<>();
        params.put("accessToken", jwtTokenResponse.getAccessToken());
        params.put("refreshToken", jwtTokenResponse.getRefreshToken());

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        log.info("■ 5. 폼당폼당 로그인 URL 생성");
        return formdang_success_login
                + "?"
                + paramStr; // 폼당폼당 관리자 메인 페이지 URL
    }

    @Override
    public String successPaperLogin(AdminTbEntity adminTb) {
        log.info("■ 4. 폼당폼당 로그인 토큰 생성");
        JwtTokenResponse jwtTokenResponse = tokenService.getLoginToken(String.valueOf(adminTb.getAid()), adminTb.getName(), adminTb.getProfile(), accessKey); // 폼당폼당 JWT 토큰 요청
        Map<String, Object> params = new HashMap<>();
        params.put("accessToken", jwtTokenResponse.getAccessToken());
        params.put("refreshToken", jwtTokenResponse.getRefreshToken());

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
    public boolean updateProfile(Long aid, GlobalFile profile, MultipartFile file) {
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
