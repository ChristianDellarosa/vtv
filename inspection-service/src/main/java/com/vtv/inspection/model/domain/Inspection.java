package com.vtv.inspection.model.domain;

import com.vtv.inspection.service.check.CheckableStep;
import com.vtv.inspection.service.check.InspectionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inspection {
    private String carPlate;
    private String clientEmail;
    private ZonedDateTime dateTime;
    private AppointmentType appointmentType;
    private InspectionStatus status;
    private InspectionResult result;
    private List<CheckableStep> checkSteps;

    public Integer getScoreResult() { //TODO: Calcula el score total de todos los pasos
        return null;
    }
}
