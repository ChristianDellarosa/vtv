package com.vtv.inspection.model.document;

import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.service.check.CheckableStep;
import com.vtv.inspection.model.domain.InspectionStatus;
import com.vtv.inspection.service.check.InspectionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inspections")
public class InspectionDocument {
    @MongoId
    private String id;
    private String carPlate;
    private String clientEmail;
    private ZonedDateTime dateTime;
    private AppointmentType appointmentType;
    private InspectionStatus status;
    private Integer score;
    private InspectionResult result;
}
