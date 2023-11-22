package com.vtv.inspection.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;

import static com.vtv.inspection.utils.DateUtils.ZONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DateUtilsTest {
    @Test
    public void getZoneIdTest() {
       assertEquals(ZoneId.of(ZONE), DateUtils.getZoneId());
    }
}
