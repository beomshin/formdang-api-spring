package com.kr.formdang.controller;

import com.kr.formdang.crypto.HashNMacUtil;
import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.LoginException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.external.AuthClient;
import com.kr.formdang.external.dto.auth.JwtTokenRequest;
import com.kr.formdang.external.dto.auth.JwtTokenResponse;
import com.kr.formdang.external.dto.google.GoogleLoginResponse;
import com.kr.formdang.enums.AdminTypeEnum;
import com.kr.formdang.external.dto.google.GoogleTokenRequest;
import com.kr.formdang.prop.GoogleProp;
import com.kr.formdang.prop.LoginProp;
import com.kr.formdang.service.admin.AdminService;
import com.kr.formdang.service.client.GoogleService;
import com.kr.formdang.utils.ClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GoogleController {

    @Value("${token.access-key}")
    private String accessKey;

    private final LoginProp loginProp;
    private final GoogleProp googleProp;
    private final GoogleService googleService;
    private final AdminService adminService;
    private final AuthClient authClient;

    /**
     * 구글 로그인 페이지 이동 API
     */
    @GetMapping(value = "/public/google/login")
    public RedirectView moveGoogleInitUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER); // 303 상태 처리
        redirectView.setUrl(googleProp.googleInitUrl()); // 이동 URL 세팅
        return redirectView;
    }

    /**
     * 유저 설문 폼 페이지 비로그인시 구글 로그인 이동 API
     */
    @GetMapping(value = "/public/google/paper/login")
    public RedirectView moveGoogleInitPaperUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER); // 303 상태 처리
        redirectView.setUrl(googleProp.googleInitPaperUrl()); // 이동 URL 세팅
        return redirectView;
    }

    /**
     * 구글 로그인 CALL BACK API (메인 페이지 로그인)
     *
     * 구글 로그인 페이지 이동 후 유저가 로그인 이벤트 발생 시 제어권을 해당 콜백 API 로 전달
     * 구글 코드로 토큰, 유저 정보 조회 후, 폼당 서버 가입 및 토큰 발급 진행 후 완료 페이지 이동
     */
    @GetMapping(value = "/public/google/login/callback")
    public RedirectView redirectGoogleLogin(@RequestParam(value = "code") String code, RedirectView redirectView) throws LoginException {

        try {
            log.info("■ 1. 구글 로그인 콜백 요청 성공");
            final String id = "G" + UUID.randomUUID().toString().substring(0, 31);
            GoogleTokenRequest request = new GoogleTokenRequest(googleProp.getGoogleClientId(), googleProp.getGoogleSecret(), googleProp.getGoogleRedirectUri(), code);
            GoogleLoginResponse googleLoginResponse = googleService.googleOAuth(request); // 구글 로그인 정보 취득

            log.info("■ 2. 구글 로그인 API 요청");
            AdminTbEntity adminTbEntity = AdminTbEntity.builder()
                    .id(id)
                    .subId(googleLoginResponse.getSub())
                    .pw(HashNMacUtil.getHashSHA256(StringUtils.reverse(id)))
                    .name(googleLoginResponse.getName())
                    .type(AdminTypeEnum.GOOGLE_TYPE.getCode())
                    .build();

            log.info("■ 3. 구글 로그인 정보 저장");
            adminTbEntity = adminService.saveSnsAdmin(adminTbEntity); // 폼당폼당 로그인 및 가입

            log.info("■ 4. 구글 로그인 인증서버 토큰 발급 요청");
            JwtTokenResponse jwtTokenResponse = (JwtTokenResponse)
                    authClient.requestToken(new JwtTokenRequest(String.valueOf(adminTbEntity.getAid()), accessKey, adminTbEntity.getName(), adminTbEntity.getProfile())); // 폼당폼당 JWT 토큰 요청

            if (jwtTokenResponse == null || jwtTokenResponse.isFail()) {
                log.error("■ 인증 토큰 발급 실패 확인 필요");
                throw new LoginException(ResultCode.FAIL_GOOGLE_LOGIN, loginProp.getFailUrl() + "?fail=true");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("accessToken", jwtTokenResponse.getAccessToken());
            params.put("refreshToken", jwtTokenResponse.getAccessToken());

            redirectView.setUrl(loginProp.getSuccessUrl() + "?" + ClientUtils.convertMapToParam(params)); // 폼당폼당 로그인 성공 페이지 세팅

            log.info("■ 5. 구글 로그인 콜백 리다이렉트 : {}", redirectView.getUrl());
            return redirectView;

        } catch (Throwable e) {
            log.error("■ 메인 페이지 구글 로그인 오류", e);
            throw new LoginException(ResultCode.FAIL_GOOGLE_LOGIN, loginProp.getFailUrl() + "?fail=true");
        }
    }


    /**
     * 구글 로그인 CALL BACK API (유저 설문 폼 페이지 로그인)
     *
     * 구글 로그인 페이지 이동 후 유저가 로그인 이벤트 발생 시 제어권을 해당 콜백 API 로 전달
     * 구글 코드로 토큰, 유저 정보 조회 후, 폼당 서버 가입 및 토큰 발급 진행 후 완료 페이지 이동
     */
    @GetMapping(value = "/public/google/login/paper/callback")
    public RedirectView redirectGooglePaperLogin(@RequestParam(value = "code") String code, RedirectView redirectView) throws LoginException {

        try {

            log.info("■ 1. 구글 페이퍼 로그인 콜백 요청 성공");
            final String id = "G" + UUID.randomUUID().toString().substring(0, 31);

            log.info("■ 2. 구글 로그인 API 요청");
            GoogleTokenRequest request = new GoogleTokenRequest(googleProp.getGoogleClientId(), googleProp.getGoogleSecret(), googleProp.getGoogleRedirectPaperUri(), code);
            GoogleLoginResponse googleLoginResponse = googleService.googleOAuth(request); // 구글 로그인 정보 취득

            AdminTbEntity adminTbEntity = AdminTbEntity.builder()
                    .id(id)
                    .subId(googleLoginResponse.getSub())
                    .pw(HashNMacUtil.getHashSHA256(StringUtils.reverse(id)))
                    .name(googleLoginResponse.getName())
                    .type(AdminTypeEnum.GOOGLE_TYPE.getCode())
                    .build();

            log.info("■ 3. 구글 로그인 정보 저장");
            adminTbEntity = adminService.saveSnsAdmin(adminTbEntity); // 폼당폼당 로그인 및 가입

            log.info("■ 4. 구글 로그인 인증서버 토큰 발급 요청");
            JwtTokenResponse jwtTokenResponse = (JwtTokenResponse)
                    authClient.requestToken(new JwtTokenRequest(String.valueOf(adminTbEntity.getAid()), accessKey, adminTbEntity.getName(), adminTbEntity.getProfile())); // 폼당폼당 JWT 토큰 요청

            if (jwtTokenResponse == null || jwtTokenResponse.isFail()) {
                log.error("■ 인증 토큰 발급 실패 확인 필요");
                throw new LoginException(ResultCode.FAIL_GOOGLE_LOGIN, loginProp.getFailUrl() + "?fail=true");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("accessToken", jwtTokenResponse.getAccessToken());
            params.put("refreshToken", jwtTokenResponse.getAccessToken());

            redirectView.setUrl(loginProp.getSuccessPaperUrl() + "?" + ClientUtils.convertMapToParam(params)); // 폼당폼당 로그인 성공 페이지 세팅

            log.info("■ 5. 구글 페이퍼 로그인 콜백 리다이렉트 : {}", redirectView.getUrl());
            return redirectView;

        } catch (Throwable e) {
            log.error("■ 구글 유저 설문 페이지 로그인 오류", e);
            throw new LoginException(ResultCode.FAIL_GOOGLE_LOGIN, loginProp.getFailUrl() + "?fail=true");
        }
    }

}
