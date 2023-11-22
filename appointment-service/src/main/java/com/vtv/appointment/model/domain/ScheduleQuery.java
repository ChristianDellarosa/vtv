package com.vtv.appointment.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleQuery { //TODO: Quizas es un record
    private Integer dayNumber;
    private Month month;
    private Integer hour;
}
