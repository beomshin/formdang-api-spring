package com.kr.formdang.controller;

import com.kr.formdang.crypto.HashNMacUtil;
import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.external.AuthClient;
import com.kr.formdang.external.dto.auth.JwtTokenRequest;
import com.kr.formdang.external.dto.auth.JwtTokenResponse;
import com.kr.formdang.external.dto.google.GoogleLoginResponse;
import com.kr.formdang.external.dto.google.GoogleTokenRequest;
import com.kr.formdang.enums.AdminTypeEnum;
import com.kr.formdang.prop.GoogleProp;
import com.kr.formdang.dto.AdminDataDto;
import com.kr.formdang.service.admin.AdminDataService;
import com.kr.formdang.service.admin.AdminService;
import com.kr.formdang.service.client.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GoogleController {

    @Value("${token.access-key}")
    private String accessKey;

    private final GoogleProp googleProp;
    private final GoogleService googleService;
    private final AdminService adminService;
    private final AdminDataService adminDataServiceImpl;
    private final AuthClient authClient;

    /**
     * 구글 로그인 페이지 이동 API
     *
     * - 구글 로그인 페이지 포워딩
     * @return
     */
    @GetMapping(value = "/public/google/login")
    public RedirectView moveGoogleInitUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER); // 303 상태 처리
        redirectView.setUrl(googleProp.googleInitUrl()); // 이동 URL 세팅
        return redirectView;
    }


    @GetMapping(value = "/public/google/paper/login")
    public RedirectView moveGoogleInitPaperUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER); // 303 상태 처리
        redirectView.setUrl(googleProp.googleInitPaperUrl()); // 이동 URL 세팅
        return redirectView;
    }

    /**
     * 구글 로그인 콜백 API
     *
     * - 폼당폼당 페이지로 포워딩
     *  - 성공시 : 관리자 메인 페이지
     *  - 실패시 : 로그인 페이지
     *
     * - 구글 로그인 정보 취득 절차
     *  - 1. 콜백 코드를 통한 JWT 토큰 발급
     *  - 2. JWT 토큰을 통한 로그인 정보 취득
     *
     * - 로그인 정보를 통한 폼당폼당 로그인 및 가입 처리
     *
     * @param code
     * @return
     */
    @GetMapping(value = "/public/google/login/callback")
    public RedirectView redirectGoogleLogin(@RequestParam(value = "code") String code) {
        RedirectView redirectView = new RedirectView();
        try {
            log.info("■ 1. 구글 로그인 콜백 요청 성공");
            final String id = "G" + UUID.randomUUID().toString().substring(0, 31);
            GoogleLoginResponse googleLoginResponse = googleService.googleOAuth(new GoogleTokenRequest(googleProp.getGoogleClientId(), googleProp.getGoogleSecret(), googleProp.getGoogleRedirectUri(), code)); // 구글 로그인 정보 취득
            AdminTbEntity adminTb = adminService.saveSnsAdmin(adminDataServiceImpl.getAdminData(AdminDataDto.builder()
                    .id(id)
                    .pw(HashNMacUtil.getHashSHA256(StringUtils.reverse(id)))
                    .type(AdminTypeEnum.GOOGLE_TYPE.getCode())
                    .name(googleLoginResponse.getName())
                    .sub_id(googleLoginResponse.getSub())
                    .build())); // 폼당폼당 로그인 및 가입
            JwtTokenRequest jwtTokenRequest = new JwtTokenRequest(String.valueOf(adminTb.getAid()), accessKey, adminTb.getName(), adminTb.getProfile());
            JwtTokenResponse jwtTokenResponse = (JwtTokenResponse) authClient.requestToken(jwtTokenRequest); // 폼당폼당 JWT 토큰 요청

            redirectView.setUrl(adminService.successLogin(jwtTokenResponse.getAccessToken(), jwtTokenResponse.getRefreshToken())); // 폼당폼당 로그인 성공 페이지 세팅
            log.info("■ 6. 구글 로그인 콜백 리다이렉트 : {}", redirectView.getUrl());
            return redirectView;
        } catch (Exception e) {
            log.error("■ 구글 로그인 콜백 요청 오류", e);
            redirectView.setUrl(adminService.failLogin(e)); // 폼당폼당 로그인 실패 페이지 세팅
            return redirectView;
        }
    }


    @GetMapping(value = "/public/google/login/paper/callback")
    public RedirectView redirectGooglePaperLogin(@RequestParam(value = "code") String code) {
        RedirectView redirectView = new RedirectView();
        try {
            log.info("■ 1. 구글 페이퍼 로그인 콜백 요청 성공");
            final String id = "G" + UUID.randomUUID().toString().substring(0, 31);
            GoogleLoginResponse googleLoginResponse = googleService.googleOAuth(new GoogleTokenRequest(googleProp.getGoogleClientId(), googleProp.getGoogleSecret(), googleProp.getGoogleRedirectPaperUri(), code)); // 구글 로그인 정보 취득
            AdminTbEntity adminTb = adminService.saveSnsAdmin(adminDataServiceImpl.getAdminData(AdminDataDto.builder()
                    .id(id)
                    .pw(HashNMacUtil.getHashSHA256(StringUtils.reverse(id)))
                    .type(AdminTypeEnum.GOOGLE_TYPE.getCode())
                    .name(googleLoginResponse.getName())
                    .sub_id(googleLoginResponse.getSub())
                    .build())); // 폼당폼당 로그인 및 가입
            JwtTokenRequest jwtTokenRequest = new JwtTokenRequest(String.valueOf(adminTb.getAid()), accessKey, adminTb.getName(), adminTb.getProfile());
            JwtTokenResponse jwtTokenResponse = (JwtTokenResponse) authClient.requestToken(jwtTokenRequest); // 폼당폼당 JWT 토큰 요청
            redirectView.setUrl(adminService.successPaperLogin(jwtTokenResponse.getAccessToken(), jwtTokenResponse.getRefreshToken())); // 폼당폼당 로그인 성공 페이지 세팅
            log.info("■ 6. 구글 페이퍼 로그인 콜백 리다이렉트 : {}", redirectView.getUrl());
            return redirectView;
        } catch (Exception e) {
            log.error("■ 구글 페이퍼 로그인 콜백 요청 오류", e);
            redirectView.setUrl(adminService.failLogin(e)); // 폼당폼당 로그인 실패 페이지 세팅
            return redirectView;
        }
    }

}
