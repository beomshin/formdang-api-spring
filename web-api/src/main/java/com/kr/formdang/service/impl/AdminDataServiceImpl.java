package com.kr.formdang.service.impl;

import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.enums.AdminTypeEnum;
import com.kr.formdang.model.layer.AdminDataDto;
import com.kr.formdang.service.AdminDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminDataServiceImpl implements AdminDataService {

    @Override
    public AdminTbEntity getAdminData(AdminDataDto adminDataDto) {
        return AdminTbEntity.builder()
                .id(adminDataDto.getId())
                .pw(adminDataDto.getPw())
                .type(AdminTypeEnum.NORMAL_TYPE.getCode())
                .build();
    }

}
