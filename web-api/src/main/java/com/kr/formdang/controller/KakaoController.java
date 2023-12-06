package com.kr.formdang.controller;

import com.kr.formdang.model.external.kakao.KakaoLoginDto;
import com.kr.formdang.model.external.kakao.KakaoLoginRequestDto;
import com.kr.formdang.model.external.kakao.KakaoProp;
import com.kr.formdang.model.layer.AdminDataDto;
import com.kr.formdang.service.AdminDataService;
import com.kr.formdang.service.AdminService;
import com.kr.formdang.service.KakaoService;
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
public class KakaoController {

    private final KakaoProp kakaoProp;
    private final KakaoService kakaoService;
    private final AdminService adminService;
    private final AdminDataService adminDataServiceImpl;

    @GetMapping(value = "/public/kakao/login")
    public RedirectView moveGoogleInitUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        redirectView.setUrl(kakaoProp.kakaoInitUrl());
        return redirectView;
    }

    @GetMapping(value = "/public/kakao/login/callback")
    public RedirectView redirectKakaoLogin(@RequestParam(value = "code") String code) throws Exception{
        RedirectView redirectView = new RedirectView();
        try {
            KakaoLoginDto kakaoLoginDto = kakaoService.kakaoOAuth(new KakaoLoginRequestDto(kakaoProp, code));
            adminService.saveSnsAdmin(adminDataServiceImpl.getAdminData(new AdminDataDto(kakaoLoginDto)));
            redirectView.setUrl(kakaoProp.getFormdang_form_list());
            return redirectView;
        } catch (Exception e) {
            log.error("{}", e);
            redirectView.setUrl(kakaoProp.getFormdang_login());
            return redirectView;
        }
    }

}
