package com.kr.formdang.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindFormDto {

    private Long offset;
    private Integer pageSize;
    private Long aid;
    private Integer type;
    private Integer endFlag;
    private Integer delFlag;
    private Integer status;
    private Integer order;



}
