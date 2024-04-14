package com.kr.formdang.utils.file;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

    /**
     * 파일 확장자 가져오기
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            log.error("[잘못된 형식의 파일] ================> ");
            return null;
        }
    }
}
