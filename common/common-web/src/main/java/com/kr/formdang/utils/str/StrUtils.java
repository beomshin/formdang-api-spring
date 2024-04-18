package com.kr.formdang.utils.str;


import java.util.Arrays;
import java.util.stream.Collectors;

public class StrUtils {

    public static String joining(String[] target, String delimiter) {
        if (target == null || target.length == 0) return null;
        return String.join(delimiter != null ? delimiter : "|", target);
    }

    public static String joining(String[] target) {
        return joining(target, "|");
    }

    public static String[] split(String target, String separator) {
        if (target == null || target.isEmpty()) return null;
        return target.split(separator != null ? separator : "\\|");
    }

    public static String[] split(String target) {
        return split(target, "\\|");
    }
}
