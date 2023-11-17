package com.vtv.appointment.util;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public final static int HOUR_ZERO = 0;
    public final static int HOUR_TWENTY_TREE = 23;
    public final static int SECOND_ZERO = 0;
    public final static int SECOND_FIFTY_NINE = 59;
    public final static int MINUTE_ZERO = 0;
    public final static int MINUTE_FIFTY_NINE = 59;
    public final static int FIRST_DAY_OF_MONTH = 1;

    public final static int LAST_DAY_OF_MONTH = 31;

    public final static int FIRST_MONTH = 1;

    public final static int LAST_MONTH = 12;
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
