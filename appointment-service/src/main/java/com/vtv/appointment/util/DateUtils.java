package com.vtv.appointment.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

public class DateUtils {
    public static Integer getCurrentlyYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }


    public static ZoneId getZoneId() {
       return ZoneId.of("America/Buenos_Aires");
    }
}
