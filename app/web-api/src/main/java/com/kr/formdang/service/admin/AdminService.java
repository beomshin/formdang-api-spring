package com.kr.formdang.service.admin;


import com.kr.formdang.entity.AdminTbEntity;
import com.kr.formdang.exception.FormException;
import com.kr.formdang.model.file.S3File;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    AdminTbEntity saveSnsAdmin(AdminTbEntity adminTbEntity) throws FormException;
    void updateProfile(Long aid, S3File profile, MultipartFile file);

}
