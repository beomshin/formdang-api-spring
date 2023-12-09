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

    @GetMapping(value = "/public/google/login")
    public RedirectView moveGoogleInitUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        redirectView.setUrl(googleProp.googleInitUrl());
        return redirectView;
    }

    @GetMapping(value = "/public/google/login/callback")
    public RedirectView redirectGoogleLogin(@RequestParam(value = "code") String code) {
        RedirectView redirectView = new RedirectView();
        try {
            GoogleLoginDto googleLoginDto = googleService.googleOAuth(new GoogleLoginRequestDto(googleProp, code));
            AdminTbEntity adminTb = adminService.saveSnsAdmin(adminDataServiceImpl.getAdminData(new AdminDataDto(googleLoginDto)));
            redirectView.setUrl(adminService.successLogin(adminTb));
            return redirectView;
        } catch (Exception e) {
            log.error("{}", e);
            redirectView.setUrl(adminService.failLogin(e));
            return redirectView;
        }
    }

}
