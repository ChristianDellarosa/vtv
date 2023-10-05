package com.vtv.appointment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateTimeFilter {
    Integer year; //TODO: Quizas este al pedo y hago current year
    Integer dayNumber; //TODO: De 1 a 31
    Month month;
    String hour; //TODO: Si viene hora tiene que venir dia, dia es numerico, etc para conformar una fecha es dayNumber/Month/YEAR
}

//Strategies
//1 Month and day
//2 Month and day and hour
//3 sin filtros -> traigo todos los turnos del anio
// Cualquier otra combinacion bad request