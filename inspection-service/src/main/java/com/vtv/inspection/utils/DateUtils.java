package com.vtv.inspection.utils;

import java.time.ZoneId;
import java.util.Calendar;

public final class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String ZONE = "America/Buenos_Aires";
    public static ZoneId getZoneId() {
       return ZoneId.of(ZONE);
    }
}
