package com.vtv.inspection.model.document;

import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.InspectionStatus;
import com.vtv.inspection.model.domain.InspectionResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inspections")
public class InspectionDocument {

    public final static String CAR_PLATE_NAME_FIELD = "carPlate";
    public final static String APPOINTMENT_TYPE_NAME_FIELD = "appointmentType";

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
