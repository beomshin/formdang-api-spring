package com.kr.formdang.service.admin;


import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.CustomException;

public interface AdminService {

    AdminTbEntity saveSnsAdmin(AdminTbEntity adminTbEntity) throws CustomException;

    String successLogin(AdminTbEntity adminTb) ;

    String failLogin(Exception e) ;

}
