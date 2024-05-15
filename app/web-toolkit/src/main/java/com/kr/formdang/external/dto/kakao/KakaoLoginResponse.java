package com.kr.formdang.external.dto.kakao;

import com.kr.formdang.external.dto.ResponseClient;


import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoLoginResponse implements ResponseClient {

    private String id;
    private String connectedAt;
    private KakaoProperties properties;
    private KakaoAcount kakaoAccount;

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class KakaoProperties {

        private String nickname;
        private String profileImage;
        private String thumbnailImage;

    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class KakaoAcount {

        private String profileNicknameNeedsAgreement;
        private String profileImageNeedsAgreement;
        private     KakaoProfile profile;
        private String hasEmail;
        private String emailNeedsAgreement;
        private String isEmailValid;
        private String isEmailVerified;
        private String email;
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class KakaoProfile {

        private String nickname;
        private String thumbnailImageUrl;
        private String profileImageUrl;
        private String isDefaultImage;
    }
}
