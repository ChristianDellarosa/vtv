package com.vtv.inspection.model.domain;

import com.vtv.inspection.service.check.InspectionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
