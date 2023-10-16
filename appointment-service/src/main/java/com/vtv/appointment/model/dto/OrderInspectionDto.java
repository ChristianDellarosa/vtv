package com.vtv.appointment.model.dto;

import com.vtv.appointment.model.domain.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInspectionDto implements Serializable {

    private String carPlate;
    private String clientEmail;
    private ZonedDateTime dateTime;
    private AppointmentType appointmentType;
    private OrderType orderType;
}
