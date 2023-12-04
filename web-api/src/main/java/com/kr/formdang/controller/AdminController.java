package com.kr.formdang.controller;

import com.kr.formdang.exception.CustomException;
import com.kr.formdang.model.layer.AdminDataDto;
import com.kr.formdang.model.net.request.AdminSignRequest;
import com.kr.formdang.model.root.DefaultResponse;
import com.kr.formdang.service.AdminDataService;
import com.kr.formdang.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AdminDataService adminDataServiceImpl;

    @PostMapping("/admin/sign")
    public ResponseEntity sign(@RequestBody AdminSignRequest request) throws CustomException {
        adminService.saveAdmin(adminDataServiceImpl.getAdminData(new AdminDataDto(request)));
        return ResponseEntity.ok().body(new DefaultResponse());
    }
}
