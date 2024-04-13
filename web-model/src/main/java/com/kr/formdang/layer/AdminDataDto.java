package com.kr.formdang.layer;

import com.kr.formdang.external.google.GoogleLoginDto;
import com.kr.formdang.external.kakao.KakaoLoginDto;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminDataDto {

    private String id;
    private String sub_id;
    private String pw;
    private String name;
    private int type;

    public AdminDataDto(KakaoLoginDto kakaoLoginDto, int type) {
        this.id = "K" + UUID.randomUUID().toString().substring(0, 31);
        this.pw = StringUtils.reverse(id);
        this.type = type;
        this.name = kakaoLoginDto.getProperties().getNickname();
        this.sub_id = kakaoLoginDto.getId();
    }

    public AdminDataDto(GoogleLoginDto googleLoginDto, int type) {
        this.id = "G" + UUID.randomUUID().toString().substring(0, 31);
        this.pw = StringUtils.reverse(id);
        this.type = type;
        this.name = googleLoginDto.getName();
        this.sub_id = googleLoginDto.getSub();
    }
}
