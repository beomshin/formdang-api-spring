package com.kr.formdang.controller.sns;

import com.kr.formdang.external.google.GoogleLoginDto;
import com.kr.formdang.external.google.GoogleLoginRequestDto;
import com.kr.formdang.external.google.GoogleProp;
import com.kr.formdang.service.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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


    @GetMapping(value = "/public/google/login")
    public RedirectView moveGoogleInitUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        redirectView.setUrl(googleProp.googleInitUrl());
        return redirectView;
    }

    @GetMapping(value = "/public/google/login/callback")
    public RedirectView redirectGoogleLogin(@RequestParam(value = "code") String code) throws Exception{
        RedirectView redirectView = new RedirectView();
        try {
            GoogleLoginDto googleLoginDto = googleService.googleOAuth(new GoogleLoginRequestDto(googleProp, code));
            String loginUrl = "https://www.naver.com";
            redirectView.setUrl(loginUrl);
            return redirectView;
        } catch (Exception e) {
            log.error("{}", e);
            redirectView.setUrl("https://www.naver.com");
            return redirectView;
        }
    }

}
