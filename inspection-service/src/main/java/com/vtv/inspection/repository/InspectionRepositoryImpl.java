package com.vtv.inspection.repository;

import com.vtv.inspection.model.document.InspectionDocument;
import com.vtv.inspection.model.domain.Inspection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InspectionRepositoryImpl implements InspectionRepository {
    private final MongoTemplate mongoTemplate;

    public InspectionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Inspection save(Inspection inspection) {
        var inspectionCreated = mongoTemplate.save(
                InspectionDocument.builder()
                .appointmentType(inspection.getAppointmentType())
                .carPlate(inspection.getCarPlate())
                .status(inspection.getStatus())
                .checkSteps(inspection.getCheckSteps())
                .dateTime(inspection.getDateTime())
                .build()
        );
        return null;
    }

    @Override
    public List<Inspection> getByCarPlate(String carPlate) { //TODO: Ver si devolvemos lista o el ultimo o que devolvemos
        final var inspections = mongoTemplate.find(Query.query(Criteria.where("carPlate").is(carPlate)), Inspection.class);
        return inspections.stream().map(inspection ->
                Inspection.builder()
                        .carPlate(inspection.getCarPlate())
                        .clientEmail(inspection.getClientEmail())
                        .dateTime(inspection.getDateTime())
                        .status(inspection.getStatus())
                        .clientEmail(inspection.getClientEmail())
                        .appointmentType(inspection.getAppointmentType())
                        .checkSteps(inspection.getCheckSteps())
                        .build()
        ).collect(Collectors.toList());
    }
}
