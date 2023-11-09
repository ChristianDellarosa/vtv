package com.vtv.inspection.model.dto;

import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.InspectionResult;
import com.vtv.inspection.model.domain.InspectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionDto {
    private String id;
    private String carPlate;
    private String clientEmail;
    private ZonedDateTime dateTime;
    private AppointmentType appointmentType;
    private InspectionStatus status;
    private Integer score;
    private InspectionResult result;
}
