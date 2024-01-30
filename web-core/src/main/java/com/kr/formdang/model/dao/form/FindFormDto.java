package com.kr.formdang.model.dao.form;

import com.kr.formdang.enums.PageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

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
