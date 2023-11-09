package com.vtv.inspection.repository;

import com.vtv.inspection.model.document.InspectionDocument;
import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.Inspection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InspectionRepositoryImpl implements InspectionRepository { //TODO: Handlear errores, y mappers
    private final MongoTemplate mongoTemplate;

    public InspectionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Inspection save(Inspection inspection) {
        var inspectionCreated = mongoTemplate.save(
                InspectionDocument.builder()
                        .id(inspection.getId())
                        .clientEmail(inspection.getClientEmail())
                        .appointmentType(inspection.getAppointmentType())
                        .carPlate(inspection.getCarPlate())
                        .status(inspection.getStatus())
                        .result(inspection.getResult())
                        .score(inspection.getScore())
                        .dateTime(inspection.getDateTime())
                        .build()
        );

        return Inspection.builder()
                .id(inspectionCreated.getId())
                .result(inspectionCreated.getResult())
                .score(inspectionCreated.getScore())
                .dateTime(inspectionCreated.getDateTime())
                .clientEmail(inspectionCreated.getClientEmail())
                .appointmentType(inspectionCreated.getAppointmentType())
                .carPlate(inspectionCreated.getCarPlate())
                .status(inspectionCreated.getStatus())
                .build();
    }

    @Override
    public List<Inspection> getByCarPlateAndAppointmentType(String carPlate, AppointmentType type) { //TODO: Ver si devolvemos lista o el ultimo o que devolvemos
        final var inspections = mongoTemplate.find(Query.query(Criteria
                .where("carPlate").is(carPlate)
                .and("appointmentType").is(type)), InspectionDocument.class);

        return inspections.stream()
                .map(inspectionDocument -> Inspection.builder()
                        .id(inspectionDocument.getId())
                        .carPlate(inspectionDocument.getCarPlate())
                        .clientEmail(inspectionDocument.getClientEmail())
                        .dateTime(inspectionDocument.getDateTime())
                        .status(inspectionDocument.getStatus())
                        .clientEmail(inspectionDocument.getClientEmail())
                        .appointmentType(inspectionDocument.getAppointmentType())
                        .status(inspectionDocument.getStatus())
                        .result(inspectionDocument.getResult())
                        .score(inspectionDocument.getScore())
                        .build())
                .toList();
    }

    @Override
    public List<Inspection> getByCarPlate(String carPlate) {
        final var inspections = mongoTemplate.find(
                Query.query(Criteria
                .where("carPlate").is(carPlate)), InspectionDocument.class);

        return inspections.stream()
                .map(inspectionDocument -> Inspection.builder()
                        .id(inspectionDocument.getId())
                        .carPlate(inspectionDocument.getCarPlate())
                        .clientEmail(inspectionDocument.getClientEmail())
                        .dateTime(inspectionDocument.getDateTime())
                        .status(inspectionDocument.getStatus())
                        .clientEmail(inspectionDocument.getClientEmail())
                        .appointmentType(inspectionDocument.getAppointmentType())
                        .status(inspectionDocument.getStatus())
                        .result(inspectionDocument.getResult())
                        .score(inspectionDocument.getScore())
                        .build())
                .toList();
    }
}
