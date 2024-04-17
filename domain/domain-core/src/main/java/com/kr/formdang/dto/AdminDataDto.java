package com.kr.formdang.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AdminDataDto {

    private String id;
    private String sub_id;
    private String pw;
    private String name;
    private int type;


}
