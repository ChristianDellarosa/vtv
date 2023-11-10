package com.vtv.inspection.repository;

import com.mongodb.MongoException;
import com.vtv.inspection.exception.commons.GenericDatabaseException;
import com.vtv.inspection.model.document.InspectionDocument;
import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.commons.ErrorDetail;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.vtv.inspection.model.document.InspectionDocument.CAR_PLATE_NAME_FIELD;

@Repository
@Slf4j
public class InspectionRepositoryImpl implements InspectionRepository { //TODO: Handlear errores, y mappers
    private final MongoTemplate mongoTemplate;


    private static final String ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_MESSAGE = "An error occurs when getting inspection by car plate";
    private static final Integer ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_CODE = 540;

    private static final String ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_MESSAGE = "An error occurs when getting inspection by car plate";
    private static final Integer ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_CODE = 530;

    private static final String ERROR_ON_SAVE_INSPECTION_MESSAGE = "An error occurs when generating the inspection";
    private static final Integer ERROR_ON_SAVE_INSPECTION_CODE = 520;
    public InspectionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Inspection save(Inspection inspection) {
        try {
            final var inspectionDocumentCreated = mongoTemplate.save(
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
                    .id(inspectionDocumentCreated.getId())
                    .result(inspectionDocumentCreated.getResult())
                    .score(inspectionDocumentCreated.getScore())
                    .dateTime(inspectionDocumentCreated.getDateTime())
                    .clientEmail(inspectionDocumentCreated.getClientEmail())
                    .appointmentType(inspectionDocumentCreated.getAppointmentType())
                    .carPlate(inspectionDocumentCreated.getCarPlate())
                    .status(inspectionDocumentCreated.getStatus())
                    .build();

        } catch (MongoException mongoException) {
            log.error(ERROR_ON_SAVE_INSPECTION_MESSAGE, mongoException); //TODO: Ver de que lado meter logs
            throw new GenericDatabaseException(
                    ExceptionError.builder()
                            .description(ERROR_ON_SAVE_INSPECTION_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(ERROR_ON_SAVE_INSPECTION_CODE)
                                    .message(ERROR_ON_SAVE_INSPECTION_MESSAGE)
                                    .build())
                            .build(), mongoException);
        }
    }

    @Override
    public List<Inspection> getByCarPlateAndAppointmentType(String carPlate, AppointmentType type) { //TODO: Ver si devolvemos lista o el ultimo o que devolvemos

        try {
            final var inspections = mongoTemplate.find(Query.query(Criteria
                    .where(CAR_PLATE_NAME_FIELD).is(carPlate)
                    .and(InspectionDocument.APPOINTMENT_TYPE_NAME_FIELD).is(type)), InspectionDocument.class);

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
        } catch (MongoException mongoException) {
            log.error(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_MESSAGE, mongoException);
            throw new GenericDatabaseException(
                    ExceptionError.builder()
                            .description(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_CODE)
                                    .message(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_MESSAGE)
                                    .build())
                            .build(), mongoException);
        }
    }

    @Override
    public List<Inspection> getByCarPlate(String carPlate) {
        try {
            final var inspections = mongoTemplate.find(
                    Query.query(Criteria
                            .where(CAR_PLATE_NAME_FIELD).is(carPlate)), InspectionDocument.class);

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

        } catch (MongoException mongoException) {
            log.error(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_MESSAGE, mongoException);
            throw new GenericDatabaseException(
                    ExceptionError.builder()
                            .description(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_CODE)
                                    .message(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_MESSAGE)
                                    .build())
                            .build(), mongoException);
        }

    }
}
