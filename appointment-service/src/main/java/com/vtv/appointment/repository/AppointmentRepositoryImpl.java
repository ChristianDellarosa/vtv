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
    public AppointmentDocument create(Appointment appointment) {
       return mongoTemplate.save(AppointmentDocument.builder()
                .clientEmail(appointment.getClientEmail())
                .type(appointment.getType())
                .carPlate(appointment.getCarPlate())
                .dateTime(appointment.getDateTime())
                .build());
    }

//    @Override
//    public List<Appointment> geyByMonthAndDayAndHour(String month, String day, String hour) { //TODO: LocalTIME ES EL OBJETO DE HORA!!
//        LocalDate now = LocalDate.now();
//        // Obtenemos el primer día del mes de marzo
//        LocalDateTime primerDiaMarzo = now.withMonth(5).withDayOfMonth(24).atTime(14, 0, 0);
//
//// Obtenemos el último día del mes de marzo
//        LocalDateTime ultimoDiaMarzo = now.withMonth(5).withDayOfMonth(24).atTime(14, 59, 59);
//        // Query.query(Criteria.where("fecha").gte(LocalDateTime.of(2023, 3, 1, 14, 0, 0)).lte(LocalDateTime.of(2023, 3, 31, 14, 59, 59))),
//        // Query.query(Criteria.where("fecha").gte(new Date(LocalDateTime.of(2023, 3, 1, 14, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli())).lte(new Date(LocalDateTime.of(2023, 3, 31, 14, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli()))),
//
//        final var query2 = Query.query(Criteria.where("dateTime").gte(new Date(LocalDateTime.of(2023, 5, 24, 14, 0, 0)
//                .toInstant(ZoneOffset.UTC).toEpochMilli())).lte(new Date(LocalDateTime.of(2023, 5, 24, 14, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli()))); //TODO: Esto funca, GTE y LTE solo funcionan con Dates supuessstamente
//        final var query = Query.query(Criteria.where("dateTime").gte(primerDiaMarzo).lte(ultimoDiaMarzo));
//        //        LocalDate now = LocalDate.now();
////        // Obtenemos el primer día del mes de marzo
////        LocalDateTime primerDiaMarzo = now.withMonth(5).withDayOfMonth(24).atTime(14,0,0);
////
////        // Obtenemos el último día del mes de marzo
////        LocalDateTime ultimoDiaMarzo = now.withMonth(5).withDayOfMonth(24).atTime(14,59,59);
//        // Query.query(Criteria.where("fecha").gte(LocalDateTime.of(2023, 3, 1, 14, 0, 0)).lte(LocalDateTime.of(2023, 3, 31, 14, 59, 59))),
//        // Query.query(Criteria.where("fecha").gte(new Date(LocalDateTime.of(2023, 3, 1, 14, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli())).lte(new Date(LocalDateTime.of(2023, 3, 31, 14, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli()))),
////        final var query2 = Query.query(Criteria.where("dateTime").gte(new Date(LocalDateTime.of(2023, 5, 24, 14, 0, 0)
////                .toInstant(ZoneOffset.UTC).toEpochMilli())).lte(new Date(LocalDateTime.of(2023, 5, 24, 14, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli()))); //TODO: Esto funca, GTE y LTE solo funcionan con Dates supuessstamente
////        final var query =  Query.query(Criteria.where("dateTime").gte(primerDiaMarzo).lte(ultimoDiaMarzo));
//        final var list = mongoTemplate.find(query2, AppointmentDocument.class);
//        return null;
//        //TODO: Buscar lista de citas por mes, dia y hora
//    }

//    @Override
//    public List<Appointment> getByMonthAndYear(Integer year, Integer month) { //TODO: LocalTIME ES EL OBJETO DE HORA!!
//        //TODO: Tiene que ser a partir de un mes, siempre y cuando ese mes sea despues de el tiempo en el que estamos
//
//        final LocalDateTime firstDayOfMonth = LocalDate.of(year, month, DEFAULT_DAY).with(TemporalAdjusters.firstDayOfMonth()).atTime(0, 0, 0);
//        final LocalDateTime lastDayOfMonth = LocalDate.of(year, month, DEFAULT_DAY).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
//
//        final var query = Query.query(Criteria.where(DATE_TIME_FIELD)
//                .gte((new Date(firstDayOfMonth.toInstant(ZoneOffset.UTC).toEpochMilli())))
//                .lte(new Date(lastDayOfMonth.toInstant(ZoneOffset.UTC).toEpochMilli())));
//
//        final var list = mongoTemplate.find(query, AppointmentDocument.class);
//        return null;
//    }
//TODO: ESTOS SON TURNOS TOMADOS! no turnos disponibles ojo con eso
//    @Override
//    public List<Appointment> getByDate(LocalDate date) {//TODO: Le paso una fecha ya armada y voy desde la hora que busco, o le mando la config
//        //TODO: Tiene que ser a partir de un mes, siempre y cuando ese mes sea despues de el tiempo en el que estamos
//        //TODO: VER CONFIG DE HORARIO COMO PASARSELA!!!!!! RESTRICCIONES DE HORA!!!
//        //TODO: Sino hacer una busqueda generica POR RANGO DE DESDE Y HASTA!
//        final LocalDateTime firstDayOfMonth = LocalDate.of(year, month, DEFAULT_DAY).with(TemporalAdjusters.firstDayOfMonth()).atTime(0, 0, 0);
//        final LocalDateTime lastDayOfMonth = LocalDate.of(year, month, DEFAULT_DAY).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
//
//        final var query = Query.query(Criteria.where(DATE_TIME_FIELD)
//                .gte((new Date(firstDayOfMonth.toInstant(ZoneOffset.UTC).toEpochMilli())))
//                .lte(new Date(lastDayOfMonth.toInstant(ZoneOffset.UTC).toEpochMilli())));
//
//        final var list = mongoTemplate.find(query, AppointmentDocument.class);
//        return null;
//    }

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
}
