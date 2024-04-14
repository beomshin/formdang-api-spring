package com.kr.formdang.utils.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeUtils {

    public static Timestamp getTimeStamp(String value, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return new Timestamp(sdf.parse(value).getTime());
    }
}
