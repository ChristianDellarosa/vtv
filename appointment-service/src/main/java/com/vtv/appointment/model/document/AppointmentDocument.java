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
    public static final String CLIENT_EMAIL_FIELD_NAME = "client_email";

    public static final String DATE_TIME_FIELD_NAME = "date_time";

    public static final String CAR_PLATE_FIELD_NAME = "car_plate";

    @MongoId
    private String id;

    @Field(CAR_PLATE_FIELD_NAME)
    private String carPlate;

    @Field(CLIENT_EMAIL_FIELD_NAME)
    private String clientEmail;

    @Field(DATE_TIME_FIELD_NAME)
    private ZonedDateTime dateTime;

    private AppointmentType type;
}
