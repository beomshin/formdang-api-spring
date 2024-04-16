package com.kr.formdang.service.admin;


import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.CustomException;
import com.kr.formdang.file.dto.S3File;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    AdminTbEntity saveSnsAdmin(AdminTbEntity adminTbEntity) throws CustomException;
    String successLogin(AdminTbEntity adminTb) ;
    String successPaperLogin(AdminTbEntity adminTb);
    String failLogin(Exception e) ;
    boolean updateProfile(Long aid, S3File profile, MultipartFile file);

}
