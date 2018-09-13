package com.kazkazi.mytime.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtils {

    public static Date convertLocalDateTimeToDate(LocalDateTime lDate) {
        return Date.from(lDate.atZone(ZoneId.systemDefault()).toInstant());

    }
}
