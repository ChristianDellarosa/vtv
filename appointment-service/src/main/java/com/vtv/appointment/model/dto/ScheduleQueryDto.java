package com.vtv.appointment.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Data
@NoArgsConstructor
public class ScheduleQueryDto {
    private static final int MIN_DAY_NUMBER = 1;
    private static final int MAX_DAY_NUMBER = 31;
    //TODO: Work Range
    private static final int MIN_HOUR = 9;
    private static final int MAX_HOUR = 18;

    // @JsonProperty("day_number") //TODO: Not working
    @Min(MIN_DAY_NUMBER)
    @Max(MAX_DAY_NUMBER)
    private Integer dayNumber;

    private Month month;

    @Min(MIN_HOUR)
    @Min(MAX_HOUR)
    private Integer hour;
}

//Strategies
//1 Month and day
//2 Month and day and hour
//3 sin filtros -> traigo todos los turnos del anio
// Cualquier otra combinacion bad request