package com.kr.formdang.controller;

import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.model.external.google.GoogleLoginDto;
import com.kr.formdang.model.external.google.GoogleLoginRequestDto;
import com.kr.formdang.model.external.google.GoogleProp;
import com.kr.formdang.model.layer.AdminDataDto;
import com.kr.formdang.service.admin.AdminDataService;
import com.kr.formdang.service.admin.AdminService;
import com.kr.formdang.service.api.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GoogleController {

    private final GoogleProp googleProp;
    private final GoogleService googleService;
    private final AdminService adminService;
    private final AdminDataService adminDataServiceImpl;

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
            GoogleLoginDto googleLoginDto = googleService.googleOAuth(new GoogleLoginRequestDto(googleProp, code)); // 구글 로그인 정보 취득
            AdminTbEntity adminTb = adminService.saveSnsAdmin(adminDataServiceImpl.getAdminData(new AdminDataDto(googleLoginDto))); // 폼당폼당 로그인 및 가입
            redirectView.setUrl(adminService.successLogin(adminTb)); // 폼당폼당 로그인 성공 페이지 세팅
            return redirectView;
        } catch (Exception e) {
            log.error("[구글 로그인 콜백 API][/public/kakao/login/callback] - {}", e);
            redirectView.setUrl(adminService.failLogin(e)); // 폼당폼당 로그인 실패 페이지 세팅
            return redirectView;
        }
    }

}
