package com.kr.formdang.service;

import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.model.layer.AdminDataDto;

import java.security.NoSuchAlgorithmException;

public interface AdminDataService {

    AdminTbEntity getAdminData(AdminDataDto adminDataDto) throws NoSuchAlgorithmException;
}
