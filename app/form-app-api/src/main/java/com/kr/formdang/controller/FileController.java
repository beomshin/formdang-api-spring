package com.kr.formdang.controller;

import com.kr.formdang.dto.auth.AuthUser;
import com.kr.formdang.dto.auth.JwtTokenAuthUser;
import com.kr.formdang.exception.FormException;
import com.kr.formdang.exception.ResultCode;
import com.kr.formdang.external.AuthClient;
import com.kr.formdang.external.dto.auth.JwtTokenRequest;
import com.kr.formdang.dto.RootResponse;
import com.kr.formdang.external.dto.auth.JwtTokenResponse;
import com.kr.formdang.dto.FormFile;
import com.kr.formdang.dto.req.FileListRequest;
import com.kr.formdang.dto.req.FileProfileRequest;
import com.kr.formdang.dto.res.FileProfileResponse;
import com.kr.formdang.dto.DefaultResponse;
import com.kr.formdang.service.admin.AdminService;
import com.kr.formdang.service.file.FileService;
import com.kr.formdang.service.form.FormService;
import com.kr.formdang.dto.S3File;
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

    @Value("${token.access-key}")
    private String accessKey;
    private final AuthClient authClient; // 인증 서버 http client
    private final FileService<S3File> awsS3FileService; // 파일 등록 서비스

    private final FormService formService;

    private final AdminService adminService;

    /**
     * 프로필 이미지 변경
     * @param fileRequest
     * @param token
     * @return
     */
    @PostMapping("/public/file/upload/profile")
    public ResponseEntity<RootResponse> uploadFileProfile(@ModelAttribute @Valid FileProfileRequest fileRequest, @RequestHeader("Authorization") String token) {
        log.info("■ 1. 프로필 등록 요청 성공");
        AuthUser authUser = new JwtTokenAuthUser(token);
        S3File profile = awsS3FileService.uploadSingle(fileRequest.getProfile()); // 파일 등록
        boolean result = adminService.updateProfile(authUser.getId(), profile, fileRequest.getProfile()); // 프로필 정보 업데이트
        if (result) { // 프로필 등록 성공
            log.debug("■ JWT 토큰 재발급 신청 [프로필 값 업데이트]");
            JwtTokenRequest jwtTokenRequest = new JwtTokenRequest(String.valueOf(authUser.getId()), accessKey, authUser.getName(), profile.getPath());
            JwtTokenResponse jwtTokenResponse = (JwtTokenResponse) authClient.requestToken(jwtTokenRequest); // 폼당폼당 JWT 토큰 요청 (프로필 내용 변경)
            log.info("■ 3. 프로필 등록 응답 성공");
            return ResponseEntity.ok().body(new FileProfileResponse(jwtTokenResponse.getAccessToken(), profile.getPath()));
        } else { // 프로필 등록 실패
            log.error("■ 3. 프로필 등록 응답 실패");
            return ResponseEntity.ok().body(new DefaultResponse(ResultCode.FAIL_UPLOAD_PROFILE));
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
    public ResponseEntity<RootResponse> uploadFileList(@ModelAttribute @Valid FileListRequest request, @RequestHeader("Authorization") String token, @PathVariable("fid") Long fid) throws FormException {
        log.info("■ 1. 이미지 리스트 등록 요청 성공 (fid: {})", fid);
        AuthUser authUser = new JwtTokenAuthUser(token);

        log.info("■ 2. 폼 상세 정보 조회 쿼리 시작");
        formService.findForm(authUser.getId(), fid); // 유효한 폼 유효성 처리

        List<FormFile> formFiles = new ArrayList<>();
        for (int i=0; i < request.getFiles().size(); i++) {
            formFiles.add(new FormFile(request.getFiles().get(i), request.getOrders().get(i), request.getTypes().get(i))); // 요청값 파일 리스트로 담기
        }

        log.info("■ 3. AWS 이미지 등록 비동기 처리 시작");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CompletableFuture<S3File>> futures = formFiles.stream().map(awsS3FileService::uploadSingle).collect(Collectors.toList());
        List<S3File> files = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        stopWatch.stop();

        log.info("■ 4. AWS 이미지 등록 비동기 처리 종료 (걸린 시간: {}ms)", stopWatch.getTotalTimeMillis());
        formService.updateImage(fid, files);

        return ResponseEntity.ok().body(new DefaultResponse());
    }

}
