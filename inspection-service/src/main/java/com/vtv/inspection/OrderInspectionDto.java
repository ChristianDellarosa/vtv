package com.vtv.inspection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInspectionDto {
    private String carPlate;
    private String clientEmail;
    private LocalDateTime dateTime;
    private AppointmentType type;
}