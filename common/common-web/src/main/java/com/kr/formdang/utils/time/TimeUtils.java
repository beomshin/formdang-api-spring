package com.kr.formdang.utils.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeUtils {

    public static Timestamp getTimeStamp(String value, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern != null ? pattern : "yyyyMMdd");
            return new Timestamp(sdf.parse(value).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    public static Timestamp getTimeStamp(String value) {
        return getTimeStamp(value, "yyyyMMdd");
    }
}
