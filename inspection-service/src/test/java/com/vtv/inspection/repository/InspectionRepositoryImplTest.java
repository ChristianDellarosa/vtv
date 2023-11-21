package com.vtv.inspection.repository;

import com.mongodb.MongoException;
import com.vtv.inspection.exception.commons.GenericDatabaseException;
import com.vtv.inspection.mock.InspectionFactory;
import com.vtv.inspection.model.document.InspectionDocument;
import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.commons.ErrorDetail;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static com.vtv.inspection.repository.InspectionRepositoryImpl.ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_CODE;
import static com.vtv.inspection.repository.InspectionRepositoryImpl.ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_MESSAGE;
import static com.vtv.inspection.repository.InspectionRepositoryImpl.ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_CODE;
import static com.vtv.inspection.repository.InspectionRepositoryImpl.ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_MESSAGE;
import static com.vtv.inspection.repository.InspectionRepositoryImpl.ERROR_ON_SAVE_INSPECTION_CODE;
import static com.vtv.inspection.repository.InspectionRepositoryImpl.ERROR_ON_SAVE_INSPECTION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InspectionRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private InspectionRepositoryImpl inspectionRepository;


    @Test
    public void saveInspection_successfully() {
        final var inspection = InspectionFactory.buildInspection();
        final var inspectionDocument = InspectionFactory.buildInspectionDocument();
        when(mongoTemplate.save(inspectionDocument))
                .thenReturn(inspectionDocument);

        final Inspection savedInspection = inspectionRepository.save(inspection);

        assertEquals(inspection, savedInspection);

        verify(mongoTemplate).save(inspectionDocument);
    }

    @Test
    public void saveInspection_HasError_ThrowGenericDatabaseException() {
        final var inspection = InspectionFactory.buildInspection();
        final var inspectionDocument = InspectionFactory.buildInspectionDocument();

        final MongoException mongoException = new MongoException("An error ocurred");

        when(mongoTemplate.save(inspectionDocument))
                .thenThrow(mongoException);

        final var exceptionExpected = new GenericDatabaseException(
                ExceptionError.builder()
                        .description(ERROR_ON_SAVE_INSPECTION_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(ERROR_ON_SAVE_INSPECTION_CODE)
                                .message(ERROR_ON_SAVE_INSPECTION_MESSAGE)
                                .build())
                        .build(), mongoException);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> inspectionRepository.save(inspection));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(mongoTemplate).save(inspectionDocument);
    }

    @Test
    public void getByCarPlateAndAppointmentType_successfully() {
        final var carPlate = "ABC123";
        final var appointmentType = AppointmentType.INSPECTION;
        final var inspectionDocuments = List.of(InspectionFactory.buildInspectionDocument());
        final var inspection = InspectionFactory.buildInspection();
        when(mongoTemplate.find(
                        Query.query(Criteria.where(InspectionDocument.CAR_PLATE_NAME_FIELD).is(carPlate)
                                .and(InspectionDocument.APPOINTMENT_TYPE_NAME_FIELD).is(appointmentType)),
                        InspectionDocument.class))
                .thenReturn(inspectionDocuments);

        final List<Inspection> retrievedInspections = inspectionRepository.getByCarPlateAndAppointmentType(carPlate, appointmentType);

        assertEquals(List.of(inspection), retrievedInspections);

        verify(mongoTemplate).find(any(), eq(InspectionDocument.class));
    }


    @Test
    public void getByCarPlateAndAppointmentType_HasError_ThrowGenericDatabaseException() {
        final var carPlate = "ABC123";
        final var appointmentType = AppointmentType.INSPECTION;
        final MongoException mongoException = new MongoException("An error occurred while getting inspection by car plate and appointment type");
        when(mongoTemplate.find(
                        Query.query(Criteria.where(InspectionDocument.CAR_PLATE_NAME_FIELD).is(carPlate)
                                .and(InspectionDocument.APPOINTMENT_TYPE_NAME_FIELD).is(appointmentType)),
                        InspectionDocument.class))
                .thenThrow(mongoException);

        final var exceptionExpected = new GenericDatabaseException(
                ExceptionError.builder()
                        .description(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_CODE)
                                .message(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_AND_APPOINTMENT_TYPE_MESSAGE)
                                .build())
                        .build(), mongoException);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> inspectionRepository.getByCarPlateAndAppointmentType(carPlate, appointmentType));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(mongoTemplate).find(any(), eq(InspectionDocument.class));

    }

    @Test
    public void getByCarPlate_successfully() {
        final var carPlate = "ABC123";
        final var inspectionDocuments = List.of(InspectionFactory.buildInspectionDocument());
        final var inspection = InspectionFactory.buildInspection();

        when(mongoTemplate.find(Query.query(Criteria.where(InspectionDocument.CAR_PLATE_NAME_FIELD).is(carPlate)), InspectionDocument.class))
                .thenReturn(inspectionDocuments);

        final List<Inspection> retrievedInspections = inspectionRepository.getByCarPlate(carPlate);

        assertEquals(List.of(inspection), retrievedInspections);

        verify(mongoTemplate).find(any(), eq(InspectionDocument.class));
    }

    @Test
    public void getByCarPlate_HasError_ThrowGenericDatabaseException() {
        final var carPlate = "ABC123";
        final MongoException mongoException = new MongoException("An error occurred while getting inspection by car plate and appointment type");

        when(mongoTemplate.find(Query.query(Criteria.where(InspectionDocument.CAR_PLATE_NAME_FIELD).is(carPlate)), InspectionDocument.class))
                .thenThrow(mongoException);

        final var exceptionExpected = new GenericDatabaseException(
                ExceptionError.builder()
                        .description(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_CODE)
                                .message(ERROR_ON_GET_INSPECTION_BY_CAR_PLATE_MESSAGE)
                                .build())
                        .build(), mongoException);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> inspectionRepository.getByCarPlate(carPlate));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(mongoTemplate).find(any(), eq(InspectionDocument.class));

    }

}
