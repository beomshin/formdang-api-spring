package com.kr.formdang.service.impl;

import com.kr.formdang.dao.TestDao;
import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.mapper.TestMapper;
import com.kr.formdang.model.net.request.test.TestRequest;
import com.kr.formdang.repository.AdminTbRepository;
import com.kr.formdang.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final AdminTbRepository adminTbRepository;
    private final TestMapper testMapper;

    @Override
    public void callTest(TestRequest request) {
        if (StringUtils.isBlank(request.getType()) || StringUtils.equals(request.getType(), "0")) {
            AdminTbEntity adminTbEntity = testMapper.findAdmin(Long.parseLong(request.getId()));
            log.debug("[조회 어드민] : {}", adminTbEntity);
        } else if (StringUtils.equals(request.getType(), "1")){
            adminTbRepository.save(AdminTbEntity.builder()
                    .id(request.getId())
                    .pw(request.getPw())
                    .build());
        }
    }
}
