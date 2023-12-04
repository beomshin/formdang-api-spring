package com.kr.formdang.service;


import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.CustomException;

public interface AdminService {

    void saveAdmin(AdminTbEntity adminTbEntity) throws CustomException;

}
