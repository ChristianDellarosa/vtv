package com.vtv.appointment.repository;

import com.vtv.appointment.model.document.AppointmentDocument;
import com.vtv.appointment.model.domain.Appointment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final MongoTemplate mongoTemplate;

    private static final String DATE_TIME_FIELD = "dateTime";

    public AppointmentRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Appointment create(Appointment appointment) {
       final var appointmentCreated =  mongoTemplate.save(AppointmentDocument.builder()
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
    }
    @Override
    public List<Appointment> getByDateTimeRange(ZonedDateTime from, ZonedDateTime to) {
        //TODO: Tiene que ser a partir de un mes, siempre y cuando ese mes sea despues de el tiempo en el que estamos

        final var query = Query.query(Criteria.where(DATE_TIME_FIELD)
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
    }

    @Override
    public List<Appointment> getByCarPlate(String carPlate) { //todo: Get by filters
        final var appointmentDocumentList = mongoTemplate.find(Query.query(Criteria.where("carPlate").is(carPlate)), AppointmentDocument.class);
        return appointmentDocumentList.stream().map(appointmentDocument ->
                Appointment.builder()
                        .carPlate(appointmentDocument.getCarPlate())
                        .type(appointmentDocument.getType())
                        .dateTime(appointmentDocument.getDateTime())
                        .clientEmail(appointmentDocument.getClientEmail())
                        .id(appointmentDocument.getId())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getByDateTime(ZonedDateTime dateTime) {

        final var query = Query.query(Criteria.where(DATE_TIME_FIELD).is(dateTime));

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
    }
}
