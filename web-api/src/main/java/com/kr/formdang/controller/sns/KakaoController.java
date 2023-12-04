package com.kr.formdang.controller.sns;

import com.kr.formdang.external.kakao.KakaoLoginDto;
import com.kr.formdang.external.kakao.KakaoLoginRequestDto;
import com.kr.formdang.external.kakao.KakaoProp;
import com.kr.formdang.service.KakaoService;
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
public class KakaoController {

    private final KakaoProp kakaoProp;
    private final KakaoService kakaoService;


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
