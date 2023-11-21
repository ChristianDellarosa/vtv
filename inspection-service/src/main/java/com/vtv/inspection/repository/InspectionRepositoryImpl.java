package com.vtv.inspection.repository;

import com.mongodb.MongoException;
import com.vtv.inspection.exception.commons.GenericDatabaseException;
import com.vtv.inspection.mapper.InspectionMapper;
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

    public static final String ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_MESSAGE = "An error occurs when getting inspection by car plate";
    public static final Integer ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_CODE = 540;

    public static final String ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_MESSAGE = "An error occurs when getting inspection by car plate";
    public static final Integer ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_CODE = 530;

    public static final String ERROR_ON_SAVE_INSPECTION_MESSAGE = "An error occurs when generating the inspection";
    public static final Integer ERROR_ON_SAVE_INSPECTION_CODE = 520;
    public InspectionRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Inspection save(Inspection inspection) {
        try {
            return InspectionMapper.toDomain(mongoTemplate
                    .save(InspectionMapper.toEntity(inspection)));
        } catch (MongoException mongoException) {
            log.error(ERROR_ON_SAVE_INSPECTION_MESSAGE, mongoException);
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
    public List<Inspection> getByCarPlateAndAppointmentType(String carPlate, AppointmentType type) {

        try {
            final var inspections = mongoTemplate.find(Query.query(Criteria
                    .where(CAR_PLATE_NAME_FIELD).is(carPlate)
                    .and(InspectionDocument.APPOINTMENT_TYPE_NAME_FIELD).is(type)), InspectionDocument.class);

            return inspections.stream()
                    .map(InspectionMapper::toDomain)
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
                    .map(InspectionMapper::toDomain)
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
