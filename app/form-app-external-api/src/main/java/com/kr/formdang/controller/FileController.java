package com.kr.formdang.controller;

import com.kr.formdang.exception.CustomException;
import com.kr.formdang.provider.JwtTokenProvider;
import com.kr.formdang.common.GlobalCode;
import com.kr.formdang.root.RootResponse;
import com.kr.formdang.external.auth.JwtTokenResponse;
import com.kr.formdang.service.file.dto.FormFile;
import com.kr.formdang.dto.req.FileListRequest;
import com.kr.formdang.dto.req.FileProfileRequest;
import com.kr.formdang.dto.res.FileProfileResponse;
import com.kr.formdang.root.DefaultResponse;
import com.kr.formdang.service.admin.AdminService;
import com.kr.formdang.service.api.TokenService;
import com.kr.formdang.service.file.FileService;
import com.kr.formdang.service.form.FormService;
import com.kr.formdang.service.file.dto.S3File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService<S3File> awsS3FileService;
    private final FormService formService;
    private final AdminService adminService;
    private final TokenService tokenService;
    @Value("${token.access-key}")
    private String accessKey;


    /**
     * 프로필 이미지 변경
     * @param fileRequest
     * @param token
     * @return
     */
    @PostMapping("/public/file/upload/profile")
    public ResponseEntity<RootResponse> uploadFileProfile(@ModelAttribute @Valid FileProfileRequest fileRequest, @RequestHeader("Authorization") String token) {
        try {
            log.info("■ 1. 프로필 등록 요청 성공");
            final Long aid = JwtTokenProvider.getId(token); // 관리자 아이디 세팅
            S3File profile = awsS3FileService.uploadSingle(fileRequest.getProfile()); // 파일 등록
            boolean result = adminService.updateProfile(aid, profile, fileRequest.getProfile()); // 프로필 정보 업데이트

            if (result) { // 프로필 등록 성공
                log.debug("■ JWT 토큰 재발급 신청 [프로필 값 업데이트]");
                JwtTokenResponse jwtTokenResponse = tokenService.getLoginToken(String.valueOf(aid), JwtTokenProvider.getName(token), profile.getPath(), accessKey); // 폼당폼당 JWT 토큰 요청 (프로필 내용 변경)
                log.info("■ 3. 프로필 등록 응답 성공");
                return ResponseEntity.ok().body(new FileProfileResponse(jwtTokenResponse.getAccessToken(), profile.getPath()));
            } else { // 프로필 등록 실패
                log.error("■ 3. 프로필 등록 응답 실패");
                return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.FAIL_UPLOAD_PROFILE));
            }
        } catch (CustomException e) {
            log.error("■ 프로필 등록 응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■ 프로필 등록응답 오류, {}", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }


    /**
     * 다량 파일 업로드 및 폼 데이터 업데이트
     *
     * 비동기 블로킹 방식으로 다량의 파일 AWS 업로드 처리
     * 업로드 완료후 이미지 정보 업데이트 처리
     * @param request
     * @param token
     * @param fid
     * @return
     */
    @PostMapping("/public/file/list/upload/{fid}")
    public ResponseEntity<RootResponse> uploadFileList(@ModelAttribute @Valid FileListRequest request, @RequestHeader("Authorization") String token, @PathVariable("fid") Long fid){
        try {
            log.info("■ 1. 이미지 리스트 등록 요청 성공 (fid: {})", fid);
            final Long aid = JwtTokenProvider.getId(token); // 관리자 아이디 세팅
            formService.findForm(aid, fid); // 유효한 폼 유효성 처리

            List<FormFile> formFiles = new ArrayList<>();

            for (int i=0; i < request.getFiles().size(); i++) {
                formFiles.add(new FormFile(request.getFiles().get(i), request.getOrders().get(i), request.getTypes().get(i))); // 요청값 파일 리스트로 담기
            }

            StopWatch stopWatch = new StopWatch();
            log.info("■ 3. AWS 이미지 등록 비동기 처리 시작");
            stopWatch.start();
            List<CompletableFuture<S3File>> futures = formFiles.stream().map(awsS3FileService::uploadSingle).collect(Collectors.toList());
            List<S3File> files = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
            stopWatch.stop();
            log.info("■ 4. AWS 이미지 등록 비동기 처리 종료 (걸린 시간: {}ms)", stopWatch.getTotalTimeMillis());
            formService.updateImage(fid, files);

            return ResponseEntity.ok().body(new DefaultResponse());
        } catch (CustomException e) {
            log.error("■ 이미지 리스트 등록 응답 오류", e);
            return ResponseEntity.ok().body(new DefaultResponse(e.getCode()));
        } catch (Exception e) {
            log.error("■이미지 리스트 등록 응답 오류", e);
            return ResponseEntity.ok().body(new DefaultResponse(GlobalCode.SYSTEM_ERROR));
        }
    }

}
