package com.vtv.appointment.model.dto;

import com.vtv.appointment.model.domain.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInspectionDto implements Serializable {

    private String carPlate;
    private String clientEmail;
    private LocalDateTime dateTime;
    private AppointmentType type;
}
