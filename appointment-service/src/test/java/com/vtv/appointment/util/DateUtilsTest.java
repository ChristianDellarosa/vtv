package com.vtv.appointment.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

import static com.vtv.appointment.util.DateUtils.COUNTRY;
import static com.vtv.appointment.util.DateUtils.LANGUAGE;
import static com.vtv.appointment.util.DateUtils.ZONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DateUtilsTest {

    @Test
    public void getCurrentlyYearTest() {
        assertEquals(LocalDateTime.now().getYear(), DateUtils.getCurrentlyYear());
    }

    @Test
    public void getZoneIdTest() {
       assertEquals(ZoneId.of(ZONE), DateUtils.getZoneId());
    }

    @Test
    public void getLocaleTest() {
        assertEquals(new Locale(LANGUAGE, COUNTRY), DateUtils.getLocale());
    }
}
