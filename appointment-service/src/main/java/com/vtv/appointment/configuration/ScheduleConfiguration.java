package com.vtv.appointment.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "schedules")
public class ScheduleConfiguration {
    private List<Month> monthsEnable;
    private List<DayOfWeek> daysEnable;
    private List<Integer> hoursEnable;
    private Integer perHour;
    private List<LocalDate> holidayDays;
}
