package com.kr.formdang.model;

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
