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
@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    @Value("${token.access-key}")
    private String accessKey; // 인증 서버 접근 키
    private final AuthClient authClient; // 인증 서버 http client
    private final FileService<S3File> awsS3FileService; // 파일 등록 서비스
    private final FormService formService; // 폼 서비스
    private final AdminService adminService; // 어드민 서비스

    /**
     * 프로필 이미지 변경 API
     */
    @PostMapping("/public/file/upload/profile")
    public ResponseEntity<RootResponse> uploadFileProfile(
            @ModelAttribute @Valid FileProfileRequest fileRequest,
            @RequestHeader("Authorization") String token
    ) {

        log.info("■ 1. 프로필 등록 요청 성공");

        AuthUser authUser = new JwtTokenAuthUser(token); // 토큰 정보 객체 생성

        S3File profile = awsS3FileService.uploadSingle(fileRequest.getProfile()); // AWS S3 파일 등록

        boolean result = adminService.updateProfile(authUser.getId(), profile, fileRequest.getProfile()); // 프로필 정보 업데이트 처리

        if (result) { // 프로필 등록 성공

            log.debug("■ JWT 토큰 재발급 신청 [프로필 값 업데이트]");
            JwtTokenResponse jwtTokenResponse =
                    (JwtTokenResponse) authClient.requestToken(
                            new JwtTokenRequest(String.valueOf(authUser.getId()), accessKey, authUser.getName(), profile.getPath())
                    ); // 폼당폼당 JWT 토큰 재요청 (토큰 내 프로필 변경 정보 변경을 위해 재요청 후 client 에서 토큰 변경 처리)

            log.info("■ 3. 프로필 등록 응답 성공");
            return ResponseEntity.ok().body(new FileProfileResponse(jwtTokenResponse.getAccessToken(), profile.getPath())); // 파일 등록 성공 응답

        } else { // 프로필 등록 실패
            log.info("■ 3. 프로필 등록 응답 실패");
            return ResponseEntity.ok().body(new DefaultResponse(ResultCode.FAIL_UPLOAD_PROFILE));
        }
    }


    /**
     * 다량 파일 업로드 및 폼 데이터 업데이트
     *
     * 비동기 블로킹 방식으로 다량의 파일 AWS 업로드 처리
     * 업로드 완료후 이미지 정보 업데이트 처리
     */
    @PostMapping("/public/file/list/upload/{fid}")
    public ResponseEntity<RootResponse> uploadFileList(
            @ModelAttribute @Valid FileListRequest request,
            @RequestHeader("Authorization") String token,
            @PathVariable("fid") Long fid
    ) throws FormException {

        log.info("■ 1. 이미지 리스트 등록 요청 성공 (fid: {})", fid);
        AuthUser authUser = new JwtTokenAuthUser(token); // 토큰 정보 객체 생성

        log.info("■ 2. 폼 상세 정보 조회 쿼리 시작");
        formService.findForm(authUser.getId(), fid); // 유효한 폼 유효성 처리

        // 파일, 순번, 타입 객체 생성 (요청에서 해당 순서 맞춰서 보내줌 multipart 처리를 위해 개별 처리)
        List<FormFile> formFiles = new ArrayList<>();
        for (int i=0; i < request.getFiles().size(); i++) {
            formFiles.add(new FormFile(request.getFiles().get(i), request.getOrders().get(i), request.getTypes().get(i))); // 요청값 파일 리스트로 담기
        }

        log.info("■ 3. AWS 이미지 등록 비동기 처리 시작");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CompletableFuture<S3File>> futures = formFiles.stream().map(awsS3FileService::uploadSingle).collect(Collectors.toList()); // 비동기 블록 방식으로 S3 파일 등록
        List<S3File> files = futures.stream().map(CompletableFuture::join).collect(Collectors.toList()); // 비동기 요청된 모든 S3 파일 등록이 완료되면 블록 해제
        stopWatch.stop();

        log.info("■ 4. AWS 이미지 등록 비동기 처리 종료 (걸린 시간: {}ms)", stopWatch.getTotalTimeMillis());
        formService.updateImage(fid, files); // 등록된 S3 파일 폼 정보 업데이트 (로고 또는 문항 순번을 식별로 업데이트)

        return ResponseEntity.ok().body(new DefaultResponse()); // 파일 등록 성공 응답
    }

}
