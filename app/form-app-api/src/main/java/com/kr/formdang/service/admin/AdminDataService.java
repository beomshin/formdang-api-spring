package com.kr.formdang.service.admin;

import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.layer.AdminDataDto;

import java.security.NoSuchAlgorithmException;

public interface AdminDataService {

    AdminTbEntity getAdminData(AdminDataDto adminDataDto) throws NoSuchAlgorithmException; // 관리자 엔티티 생성
}
