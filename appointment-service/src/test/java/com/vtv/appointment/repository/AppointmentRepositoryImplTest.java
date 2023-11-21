package com.vtv.appointment.repository;

import com.mongodb.MongoException;
import com.vtv.appointment.exception.commons.GenericDatabaseException;
import com.vtv.appointment.mock.AppointmentFactory;
import com.vtv.appointment.model.document.AppointmentDocument;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static com.vtv.appointment.repository.AppointmentRepositoryImpl.ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_CODE;
import static com.vtv.appointment.repository.AppointmentRepositoryImpl.ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_MESSAGE;
import static com.vtv.appointment.repository.AppointmentRepositoryImpl.ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_CODE;
import static com.vtv.appointment.repository.AppointmentRepositoryImpl.ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE;
import static com.vtv.appointment.repository.AppointmentRepositoryImpl.ERROR_ON_SAVE_APPOINTMENT_CODE;
import static com.vtv.appointment.repository.AppointmentRepositoryImpl.ERROR_ON_SAVE_APPOINTMENT_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentRepositoryImplTest {

    @InjectMocks
    private AppointmentRepositoryImpl appointmentRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Test
    public void create_successfully() {
        final Appointment appointment = AppointmentFactory.buildAppointment();
        final Appointment appointmentExpected = AppointmentFactory.buildAppointmentAfterSave();
        final AppointmentDocument appointmentDocument = AppointmentFactory.buildAppointmentDocument();
        final AppointmentDocument appointmentDocumentAfterSave = AppointmentFactory.buildAppointmentDocumentAfterSave();

        when(mongoTemplate.save(appointmentDocument)).thenReturn(appointmentDocumentAfterSave);

        final Appointment appointmentResponse = appointmentRepository.create(appointment);

        assertEquals(appointmentExpected, appointmentResponse);

        verify(mongoTemplate).save(appointmentDocument);
    }

    @Test
    public void create_hasError_Throw_GenericDatabaseException() {
        final Appointment appointment = AppointmentFactory.buildAppointment();
        final AppointmentDocument appointmentDocument = AppointmentFactory.buildAppointmentDocument();

        final var exceptionExpected = new GenericDatabaseException(
                ExceptionError.builder()
                        .description(ERROR_ON_SAVE_APPOINTMENT_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(ERROR_ON_SAVE_APPOINTMENT_CODE)
                                .message(ERROR_ON_SAVE_APPOINTMENT_MESSAGE)
                                .build())
                        .build(), null);

        when(mongoTemplate.save(appointmentDocument)).thenThrow(new MongoException("Simulated Error"));

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentRepository.create(appointment));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(mongoTemplate).save(appointmentDocument);
    }

    @Test
    public void getByDateTimeRange_successfully() {
        final ZonedDateTime from = ZonedDateTime.now();
        final ZonedDateTime to = from.plusDays(1);
        final List<Appointment> appointments = List.of(AppointmentFactory.buildAppointment());

        when(mongoTemplate.find(any(), eq(AppointmentDocument.class)))
                .thenReturn(List.of(AppointmentFactory.buildAppointmentDocument()));

        final List<Appointment> retrievedAppointments = appointmentRepository.getByDateTimeRange(from, to);

        assertEquals(appointments, retrievedAppointments);

        verify(mongoTemplate).find(any(), eq(AppointmentDocument.class));
    }

    @Test
    public void getByDateTimeRange_empty_successfully() {
        final ZonedDateTime from = ZonedDateTime.now();
        final ZonedDateTime to = from.plusDays(1);
        final List<Appointment> appointments = Collections.emptyList();

        when(mongoTemplate.find(any(), eq(AppointmentDocument.class)))
                .thenReturn(Collections.emptyList());

        final List<Appointment> retrievedAppointments = appointmentRepository.getByDateTimeRange(from, to);

        assertEquals(appointments, retrievedAppointments);

        verify(mongoTemplate).find(any(), eq(AppointmentDocument.class));
    }

    @Test
    public void getByDateTimeRange_hasError_Throw_GenericDatabaseException() {
        final ZonedDateTime from = ZonedDateTime.now();
        final ZonedDateTime to = from.plusDays(1);

        final var exceptionExpected = new GenericDatabaseException(
                ExceptionError.builder()
                        .description(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_CODE)
                                .message(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE)
                                .build())
                        .build(), null);

        when(mongoTemplate.find(any(), eq(AppointmentDocument.class)))
                .thenThrow(new MongoException("Simulated Error"));

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentRepository.getByDateTimeRange(from, to));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(mongoTemplate).find(any(), eq(AppointmentDocument.class));
    }

    @Test
    public void getByCarPlate_successfully() {
        final String carPlate = "ABC123";
        final List<Appointment> appointments = List.of(AppointmentFactory.buildAppointment());

        when(mongoTemplate.find(any(), eq(AppointmentDocument.class)))
                .thenReturn(List.of(AppointmentFactory.buildAppointmentDocument()));

        final List<Appointment> retrievedAppointments = appointmentRepository.getByCarPlate(carPlate);

        assertEquals(appointments, retrievedAppointments);

        verify(mongoTemplate).find(any(), eq(AppointmentDocument.class));
    }

    @Test
    public void getByCarPlate_hasError_Throw_GenericDatabaseException() {
        final String carPlate = "ABC123";

        final var exceptionExpected = new GenericDatabaseException(
                ExceptionError.builder()
                        .description(ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_CODE)
                                .message(ERROR_ON_GET_APPOINTMENT_BY_CAR_PLATE_MESSAGE)
                                .build())
                        .build(), null);

        when(mongoTemplate.find(any(), eq(AppointmentDocument.class)))
                .thenThrow(new MongoException("Simulated Error"));

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentRepository.getByCarPlate(carPlate));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(mongoTemplate).find(any(), eq(AppointmentDocument.class));
    }

    @Test
    public void getByDateTime_successfully() {
        final ZonedDateTime dateTime = ZonedDateTime.now();
        final List<Appointment> appointments = List.of(AppointmentFactory.buildAppointment());

        when(mongoTemplate.find(any(), eq(AppointmentDocument.class)))
                .thenReturn(List.of(AppointmentFactory.buildAppointmentDocument()));

        final List<Appointment> retrievedAppointments = appointmentRepository.getByDateTime(dateTime);

        assertEquals(appointments, retrievedAppointments);

        verify(mongoTemplate).find(any(), eq(AppointmentDocument.class));
    }

    @Test
    public void getByDateTime_hasError_Throw_GenericDatabaseException() {
        final ZonedDateTime dateTime = ZonedDateTime.now();

        final var exceptionExpected = new GenericDatabaseException(
                ExceptionError.builder()
                        .description(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_CODE)
                                .message(ERROR_ON_GET_APPOINTMENT_BY_DATE_TIME_MESSAGE)
                                .build())
                        .build(), null);

        when(mongoTemplate.find(any(), eq(AppointmentDocument.class)))
                .thenThrow(new MongoException("Simulated Error"));

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentRepository.getByDateTime(dateTime));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(mongoTemplate).find(any(), eq(AppointmentDocument.class));
    }

}
