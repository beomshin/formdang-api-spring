package com.kr.formdang.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {


    public static String getAccessFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            log.error("[잘못된 형식의 파일] ================> ");
            return null;
        }
    }
}
