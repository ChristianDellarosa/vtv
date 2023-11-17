package com.vtv.appointment.repository;

import com.mongodb.MongoException;
import com.vtv.appointment.exception.commons.GenericDatabaseException;
import com.vtv.appointment.model.document.AppointmentDocument;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.vtv.appointment.model.document.AppointmentDocument.CAR_PLATE_FIELD_NAME;
import static com.vtv.appointment.model.document.AppointmentDocument.DATE_TIME_FIELD_NAME;

@Repository
@Slf4j
public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final MongoTemplate mongoTemplate;

    public static final String ERROR_ON_SAVE_APPOINTMENT_MESSAGE = "An error occurs when save the appointment";
    public static final Integer ERROR_ON_SAVE_APPOINTMENT_CODE = 550;

    public static final String ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE = "An error occurs when getting appointment by date time";
    public static final Integer ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_CODE = 560;

    public static final String ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_MESSAGE = "An error occurs when getting appointment by car plate";
    public static final Integer ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_CODE = 570;

    public AppointmentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Appointment create(Appointment appointment) {

        try {
            final var appointmentCreated = mongoTemplate.save(
                    AppointmentDocument.builder()
                    .clientEmail(appointment.getClientEmail())
                    .type(appointment.getType())
                    .carPlate(appointment.getCarPlate())
                    .dateTime(appointment.getDateTime())
                    .build());

            return Appointment.builder() //TODO: Revisar si necesitamos un createdAt o algunas cosas de esas.
                    .clientEmail(appointmentCreated.getClientEmail())
                    .type(appointmentCreated.getType())
                    .carPlate(appointmentCreated.getCarPlate())
                    .dateTime(appointmentCreated.getDateTime())
                    .build();

        } catch (MongoException mongoException) {
            log.error(ERROR_ON_SAVE_APPOINTMENT_MESSAGE, mongoException);
            throw new GenericDatabaseException(
                    ExceptionError.builder()
                            .description(ERROR_ON_SAVE_APPOINTMENT_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(ERROR_ON_SAVE_APPOINTMENT_CODE)
                                    .message(ERROR_ON_SAVE_APPOINTMENT_MESSAGE)
                                    .build())
                            .build(), mongoException);
        }
    }

    @Override
    public List<Appointment> getByDateTimeRange(ZonedDateTime from, ZonedDateTime to) {
        //TODO: Tiene que ser a partir de un mes, siempre y cuando ese mes sea despues de el tiempo en el que estamos
        try {
        final var query = Query.query(Criteria.where(DATE_TIME_FIELD_NAME)
                .gte((Date.from(from.toInstant())))
                .lte(Date.from(to.toInstant())));

        final var appointmentDocumentList = mongoTemplate.find(query, AppointmentDocument.class);

        return appointmentDocumentList.stream().map(appointmentDocument ->
                Appointment.builder()
                        .carPlate(appointmentDocument.getCarPlate())
                        .type(appointmentDocument.getType())
                        .dateTime(appointmentDocument.getDateTime())
                        .clientEmail(appointmentDocument.getClientEmail())
                        .id(appointmentDocument.getId())
                        .build()

        ).collect(Collectors.toList());

    } catch (MongoException mongoException) {
        log.error(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE, mongoException);
        throw new GenericDatabaseException(
                ExceptionError.builder()
                        .description(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_CODE)
                                .message(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE)
                                .build())
                        .build(), mongoException);
        }
    }

    @Override
    public List<Appointment> getByCarPlate(String carPlate) {
        try {
            final var appointmentDocumentList = mongoTemplate.find(Query.query(Criteria.where(CAR_PLATE_FIELD_NAME).is(carPlate)), AppointmentDocument.class);
            return appointmentDocumentList.stream().map(appointmentDocument ->
                    Appointment.builder()
                            .carPlate(appointmentDocument.getCarPlate())
                            .type(appointmentDocument.getType())
                            .dateTime(appointmentDocument.getDateTime())
                            .clientEmail(appointmentDocument.getClientEmail())
                            .id(appointmentDocument.getId())
                            .build()
            ).collect(Collectors.toList());

        } catch (MongoException mongoException) {
            log.error(ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_MESSAGE, mongoException);
            throw new GenericDatabaseException(
                    ExceptionError.builder()
                            .description(ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_CODE)
                                    .message(ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_MESSAGE)
                                    .build())
                            .build(), mongoException);
        }
    }

    @Override
    public List<Appointment> getByDateTime(ZonedDateTime dateTime) {
        try {
            final var query = Query.query(Criteria.where(DATE_TIME_FIELD_NAME).is(dateTime));

            final var appointmentDocumentList = mongoTemplate.find(query, AppointmentDocument.class);

            return appointmentDocumentList.stream().map(appointmentDocument ->
                    Appointment.builder()
                            .carPlate(appointmentDocument.getCarPlate())
                            .type(appointmentDocument.getType())
                            .dateTime(appointmentDocument.getDateTime())
                            .clientEmail(appointmentDocument.getClientEmail())
                            .id(appointmentDocument.getId())
                            .build()

            ).collect(Collectors.toList());

        } catch (MongoException mongoException) {
            log.error(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE, mongoException);
            throw new GenericDatabaseException(
                    ExceptionError.builder()
                            .description(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_CODE)
                                    .message(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE)
                                    .build())
                            .build(), mongoException);
        }
    }
}
