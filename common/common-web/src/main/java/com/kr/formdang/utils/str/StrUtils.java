package com.kr.formdang.utils.str;


import java.util.Arrays;
import java.util.stream.Collectors;

public class StrUtils {

    public static String joining(String[] target, String delimiter) {
        if (target == null || target.length == 0) return null;
        return String.join(delimiter, target);
    }
}
