package com.kr.formdang.model.layer;

import com.kr.formdang.enums.AdminTypeEnum;
import com.kr.formdang.model.external.google.GoogleLoginDto;
import com.kr.formdang.model.external.kakao.KakaoLoginDto;
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
    private AdminTypeEnum type;

    public AdminDataDto(KakaoLoginDto kakaoLoginDto) {
        this.id = "K" + UUID.randomUUID().toString().substring(0, 31);
        this.pw = StringUtils.reverse(id);
        this.type = AdminTypeEnum.KAKAO_TYPE;
        this.name = kakaoLoginDto.getProperties().getNickname();
        this.sub_id = kakaoLoginDto.getId();
    }

    public AdminDataDto(GoogleLoginDto googleLoginDto) {
        this.id = "G" + UUID.randomUUID().toString().substring(0, 31);
        this.pw = StringUtils.reverse(id);
        this.type = AdminTypeEnum.GOOGLE_TYPE;
        this.name = googleLoginDto.getName();
        this.sub_id = googleLoginDto.getSub();
    }
}
