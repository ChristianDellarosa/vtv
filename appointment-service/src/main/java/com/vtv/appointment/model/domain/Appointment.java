package com.vtv.appointment.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data //TODO: Pasar todo a Value
public class Appointment {

    private String id;

    private String carPlate;

    private String clientEmail;

    private ZonedDateTime dateTime;

    private AppointmentType type;
}
