package com.vtv.appointment.util;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static Integer getCurrentlyYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }


    public static ZoneId getZoneId() {
       return ZoneId.of("America/Buenos_Aires");
    }

    public static Locale getLocale() {
        return new Locale("es", "AR");
    }
}
