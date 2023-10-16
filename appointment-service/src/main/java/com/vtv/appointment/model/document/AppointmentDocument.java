package com.vtv.appointment.model.document;

import com.vtv.appointment.model.domain.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "appointments") //TODO: Naming convension
public class AppointmentDocument {
    private static final String clientEmailField = "client_email";
    @MongoId
    private String id;

    private String carPlate;

    @Field(clientEmailField)
    private String clientEmail;

    private ZonedDateTime dateTime;

    private AppointmentType type;
}
