package com.vtv.inspection.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Inspection {
    private String id;
    private String carPlate;
    private String clientEmail;
    private ZonedDateTime dateTime;
    private AppointmentType appointmentType;
    private InspectionStatus status;
    private Integer score;
    private InspectionResult result;
}
