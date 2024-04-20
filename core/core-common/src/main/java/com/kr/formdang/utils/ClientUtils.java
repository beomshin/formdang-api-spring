package com.kr.formdang.utils;

import java.util.Map;
import java.util.stream.Collectors;

public class ClientUtils {


    public static String convertMapToParam(Map<String, Object> params) {
        return params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));
    }
}
