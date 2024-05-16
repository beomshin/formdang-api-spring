package com.kr.formdang.controller.login;

import com.kr.formdang.crypto.HashNMacUtil;
import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.FormLoginException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.external.HttpFormClient;
import com.kr.formdang.external.dto.auth.JwtTokenRequest;
import com.kr.formdang.external.dto.auth.JwtTokenResponse;
import com.kr.formdang.external.dto.kakao.KakaoLoginResponse;
import com.kr.formdang.enums.AdminTypeEnum;
import com.kr.formdang.external.dto.kakao.KakaoTokenRequest;
import com.kr.formdang.external.prop.KakaoProp;
import com.kr.formdang.external.prop.FormProp;
import com.kr.formdang.service.admin.AdminService;
import com.kr.formdang.service.client.KakaoService;
import com.kr.formdang.utils.ClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final FormProp formProp;
    private final KakaoProp kakaoProp;
    private final KakaoService kakaoService;
    private final AdminService adminService;
    private final HttpFormClient authClient;

    /**
     * 카카오 로그인 페이지 이동 API
     */
    @GetMapping(value = "/public/kakao/login")
    public RedirectView moveGoogleInitUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        redirectView.setUrl(kakaoProp.kakaoInitUrl());
        return redirectView;
    }

    /**
     * 유저 설문 폼 페이지 비로그인시 카카오 로그인 이동 API
     */
    @GetMapping(value = "/public/kakao/paper/login")
    public RedirectView moveGoogleInitPaperUrl() {
        RedirectView redirectView = new RedirectView();
        redirectView.setStatusCode(HttpStatus.SEE_OTHER);
        redirectView.setUrl(kakaoProp.kakaoInitPaperUrl());
        return redirectView;
    }

    /**
     * 구글 로그인 CALL BACK API (메인 페이지 로그인)
     *
     * 카카오 로그인 페이지 이동 후 유저가 로그인 이벤트 발생 시 제어권을 해당 콜백 API 로 전달
     * 카카오 코드로 토큰, 유저 정보 조회 후, 폼당 서버 가입 및 토큰 발급 진행 후 완료 페이지 이동
     */
    @GetMapping(value = "/public/kakao/login/callback")
    public RedirectView redirectKakaoLogin(@RequestParam(value = "code") String code, RedirectView redirectView) throws FormLoginException {

        try {
            log.info("■ 1. 카카오 로그인 콜백 요청 성공");
            final String id = "K" + UUID.randomUUID().toString().substring(0, 31);

            log.info("■ 2. 카카오 로그인 API 요청");
            KakaoTokenRequest request = new KakaoTokenRequest(kakaoProp.getKakaoClientId(), kakaoProp.getKakaoSecret(), kakaoProp.getKakaoRedirectLoginUri(), code);
            KakaoLoginResponse kakaoLoginResponse = kakaoService.kakaoOAuth(request); // 카카오 로그인 정보 취득

            AdminTbEntity adminTbEntity = AdminTbEntity.builder()
                    .id(id)
                    .subId(kakaoLoginResponse.getId())
                    .pw(HashNMacUtil.getHashSHA256(StringUtils.reverse(id)))
                    .name(kakaoLoginResponse.getProperties().getNickname())
                    .type(AdminTypeEnum.KAKAO_TYPE.getCode())
                    .build();

            log.info("■ 3. 카카오 로그인 정보 저장");
            adminTbEntity = adminService.saveSnsAdmin(adminTbEntity);  // 폼당폼당 로그인 및 가입

            log.info("■ 4. 카카오 로그인 인증서버 토큰 발급 요청");
            JwtTokenResponse jwtTokenResponse = authClient.requestToken(JwtTokenRequest.valueOf(adminTbEntity.getAid(), adminTbEntity.getName(), adminTbEntity.getProfile()), JwtTokenResponse.class); // 폼당폼당 JWT 토큰 요청

            if (jwtTokenResponse == null || jwtTokenResponse.isFail()) {
                log.error("■ 인증 토큰 발급 실패 확인 필요");
                throw new FormLoginException(ResultCode.FAIL_KAKAO_LOGIN, formProp.getFailUrl() + "?fail=true");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("accessToken", jwtTokenResponse.getAccessToken());
            params.put("refreshToken", jwtTokenResponse.getAccessToken());

            redirectView.setUrl(formProp.getSuccessUrl() + "?" + ClientUtils.convertMapToParam(params)); // 폼당폼당 로그인 성공 페이지 세팅

            log.info("■ 5. 카카오 로그인 콜백 리다이렉트 : {}", redirectView.getUrl());
            return redirectView;

        } catch (Exception e) {
            log.error("■ 메인 페이지 카카오 로그인 오류  ", e);
            throw new FormLoginException(ResultCode.FAIL_KAKAO_LOGIN, formProp.getFailUrl() + "?fail=true");
        }
    }


    /**
     * 카카오 로그인 CALL BACK API (유저 설문 폼 페이지 로그인)
     *
     * 카카오 로그인 페이지 이동 후 유저가 로그인 이벤트 발생 시 제어권을 해당 콜백 API 로 전달
     * 카카오 코드로 토큰, 유저 정보 조회 후, 폼당 서버 가입 및 토큰 발급 진행 후 완료 페이지 이동
     */
    @GetMapping(value = "/public/kakao/login/paper/callback")
    public RedirectView redirectKakaoLoginPaper(@RequestParam(value = "code") String code, RedirectView redirectView) throws FormLoginException {

        try {

            log.info("■ 1. 카카오 로그인 콜백 요청 성공");
            final String id = "K" + UUID.randomUUID().toString().substring(0, 31);

            log.info("■ 2. 카카오 로그인 API 요청");
            KakaoTokenRequest request = new KakaoTokenRequest(kakaoProp.getKakaoClientId(), kakaoProp.getKakaoSecret(), kakaoProp.getKakaoRedirectLoginPaperUri(), code);
            KakaoLoginResponse kakaoLoginResponse = kakaoService.kakaoOAuth(request); // 카카오 로그인 정보 취득

            AdminTbEntity adminTbEntity = AdminTbEntity.builder()
                    .id(id)
                    .subId(kakaoLoginResponse.getId())
                    .pw(HashNMacUtil.getHashSHA256(StringUtils.reverse(id)))
                    .name(kakaoLoginResponse.getProperties().getNickname())
                    .type(AdminTypeEnum.KAKAO_TYPE.getCode())
                    .build();

            log.info("■ 3. 카카오 로그인 정보 저장");
            adminTbEntity = adminService.saveSnsAdmin(adminTbEntity);  // 폼당폼당 로그인 및 가입

            log.info("■ 4. 카카오 로그인 인증서버 토큰 발급 요청");
            JwtTokenResponse jwtTokenResponse = authClient.requestToken(JwtTokenRequest.valueOf(adminTbEntity.getAid(), adminTbEntity.getName(), adminTbEntity.getProfile()), JwtTokenResponse.class); // 폼당폼당 JWT 토큰 요청

            if (jwtTokenResponse == null || jwtTokenResponse.isFail()) {
                log.error("■ 인증 토큰 발급 실패 확인 필요");
                throw new FormLoginException(ResultCode.FAIL_KAKAO_LOGIN, formProp.getFailUrl() + "?fail=true");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("accessToken", jwtTokenResponse.getAccessToken());
            params.put("refreshToken", jwtTokenResponse.getAccessToken());

            redirectView.setUrl(formProp.getSuccessPaperUrl() + "?" + ClientUtils.convertMapToParam(params)); // 폼당폼당 로그인 성공 페이지 세팅

            log.info("■ 5. 카카오 로그인 콜백 리다이렉트 : {}", redirectView.getUrl());
            return redirectView;

        } catch (Exception e) {
            log.error("■ 카카오 유저 설문 페이지 로그인 오류 ", e);
            throw new FormLoginException(ResultCode.FAIL_KAKAO_LOGIN, formProp.getFailUrl() + "?fail=true");
        }
    }

}
