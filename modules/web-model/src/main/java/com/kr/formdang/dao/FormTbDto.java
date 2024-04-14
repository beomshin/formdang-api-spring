package com.kr.formdang.dao;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FormTbDto {

    private Long fid;
    private int formType;
    private String title;
    private String logoUrl;
    private int status;
    private int endFlag;
    private int delFlag;
    private Timestamp regDt;
}
