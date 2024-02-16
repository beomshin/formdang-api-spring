package com.kr.formdang.service.admin;


import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.CustomException;
import com.kr.formdang.model.common.GlobalFile;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    AdminTbEntity saveSnsAdmin(AdminTbEntity adminTbEntity) throws CustomException;

    String successLogin(AdminTbEntity adminTb) ;

    String failLogin(Exception e) ;
    int updateProfile(Long aid, GlobalFile profile, MultipartFile file);
    String getToken(String token);

}
