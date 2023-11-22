package com.vtv.appointment.service;

import com.vtv.appointment.exception.AppointmentAlreadyExistsException;
import com.vtv.appointment.exception.AppointmentErrorException;
import com.vtv.appointment.exception.InvalidAppointmentDateTimeException;
import com.vtv.appointment.exception.OrderInspectionErrorException;
import com.vtv.appointment.exception.commons.GenericDatabaseException;
import com.vtv.appointment.mock.AppointmentFactory;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import com.vtv.appointment.model.dto.OrderType;
import com.vtv.appointment.repository.AppointmentRepository;
import com.vtv.appointment.service.inspection.InspectionProducerService;
import com.vtv.appointment.service.schedule.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.vtv.appointment.service.AppointmentServiceImpl.APPOINTMENT_ALREADY_EXISTS_CODE;
import static com.vtv.appointment.service.AppointmentServiceImpl.APPOINTMENT_ALREADY_EXISTS_MESSAGE;
import static com.vtv.appointment.service.AppointmentServiceImpl.CREATE_APPOINTMENT_ERROR_CODE;
import static com.vtv.appointment.service.AppointmentServiceImpl.CREATE_APPOINTMENT_ERROR_MESSAGE;
import static com.vtv.appointment.service.AppointmentServiceImpl.FULL_BOOKING_APPOINTMENT_DATE_TIME_CODE;
import static com.vtv.appointment.service.AppointmentServiceImpl.FULL_BOOKING_APPOINTMENT_DATE_TIME_MESSAGE;
import static com.vtv.appointment.service.AppointmentServiceImpl.INVALID_APPOINTMENT_DATE_CODE;
import static com.vtv.appointment.service.AppointmentServiceImpl.INVALID_APPOINTMENT_DATE_MESSAGE;
import static com.vtv.appointment.service.inspection.InspectionProducerServiceImpl.INSPECTION_PRODUCER_ERROR_CODE;
import static com.vtv.appointment.service.inspection.InspectionProducerServiceImpl.INSPECTION_PRODUCER_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {


    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private InspectionProducerService inspectionProducerService;


    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    public void createAppointment_withValidAppointment_shouldCreateAppointmentAndSendInspectionOrder() {
        final Appointment appointment = AppointmentFactory.buildAppointment();

        when(appointmentRepository.getByCarPlate(appointment.getCarPlate())).thenReturn(Collections.emptyList());
        when(scheduleService.isValidDate(appointment.getDateTime())).thenReturn(true);
        when(scheduleService.isValidTime(appointment.getDateTime().toLocalTime())).thenReturn(true);
        when(scheduleService.isAvailableDateTime(appointment.getDateTime())).thenReturn(true);
        when(appointmentRepository.create(appointment)).thenReturn(appointment);
        doNothing().when(inspectionProducerService).orderInspection(any());

        final Appointment createdAppointment = appointmentService.create(appointment);

        assertThat(createdAppointment).isEqualTo(appointment);

        ArgumentCaptor<OrderInspectionDto> orderInspectionDtoArgumentCaptor = ArgumentCaptor.forClass(OrderInspectionDto.class);
        verify(inspectionProducerService).orderInspection(orderInspectionDtoArgumentCaptor.capture());
        OrderInspectionDto orderInspectionDto = orderInspectionDtoArgumentCaptor.getValue();
        assertThat(orderInspectionDto.getClientEmail()).isEqualTo(appointment.getClientEmail());
        assertThat(orderInspectionDto.getCarPlate()).isEqualTo(appointment.getCarPlate());
        assertThat(orderInspectionDto.getDateTime()).isEqualTo(appointment.getDateTime());
        assertThat(orderInspectionDto.getOrderType()).isEqualTo(OrderType.CREATE);
        assertThat(orderInspectionDto.getAppointmentType()).isEqualTo(appointment.getType());

        verify(appointmentRepository).getByCarPlate(appointment.getCarPlate());
        verify(scheduleService).isValidDate(appointment.getDateTime());
        verify(scheduleService).isValidTime(appointment.getDateTime().toLocalTime());
        verify(scheduleService).isAvailableDateTime(appointment.getDateTime());
        verify(appointmentRepository).create(appointment);
    }

    @Test
    public void createAppointment_case1_withAppointmentHavingAlreadyAnInspection_shouldThrowAppointmentAlreadyExistsException() {
        final Appointment appointment = AppointmentFactory.buildAppointment();

        when(appointmentRepository.getByCarPlate(appointment.getCarPlate())).thenReturn(List.of(appointment));

        final var message = String.format(APPOINTMENT_ALREADY_EXISTS_MESSAGE, appointment.getDateTime().toString());
        final var exceptionExpected = new AppointmentAlreadyExistsException(
                ExceptionError.builder()
                        .description(message)
                        .errorDetail(ErrorDetail.builder()
                                .code(APPOINTMENT_ALREADY_EXISTS_CODE)
                                .message(message)
                                .build())
                        .build());

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentService.create(appointment));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(appointmentRepository).getByCarPlate(appointment.getCarPlate());
        verify(scheduleService, never()).isValidDate(appointment.getDateTime());
        verify(scheduleService, never()).isValidTime(appointment.getDateTime().toLocalTime());
        verify(scheduleService, never()).isAvailableDateTime(appointment.getDateTime());
        verify(appointmentRepository, never()).create(appointment);
        verify(inspectionProducerService, never()).orderInspection(any());

    }

    @Test
    public void createAppointment_case_3_withAppointmentHasDateTimeInvalid_shouldThrowInvalidAppointmentDateTimeException() {
        final Appointment appointment = AppointmentFactory.buildAppointment();

        when(appointmentRepository.getByCarPlate(appointment.getCarPlate())).thenReturn(Collections.emptyList());
        when(scheduleService.isValidDate(appointment.getDateTime())).thenReturn(false);

        final var exceptionExpected = new InvalidAppointmentDateTimeException(
                ExceptionError.builder()
                        .description(INVALID_APPOINTMENT_DATE_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(INVALID_APPOINTMENT_DATE_CODE)
                                .message(INVALID_APPOINTMENT_DATE_MESSAGE)
                                .build())
                        .build());

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentService.create(appointment));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(appointmentRepository).getByCarPlate(appointment.getCarPlate());
        verify(scheduleService).isValidDate(appointment.getDateTime());
        verify(scheduleService, never()).isValidTime(appointment.getDateTime().toLocalTime());
        verify(scheduleService, never()).isAvailableDateTime(appointment.getDateTime());
        verify(appointmentRepository, never()).create(appointment);
        verify(inspectionProducerService, never()).orderInspection(any());
    }

    @Test
    public void createAppointment_case_4_withAppointmentHasFullBooking_shouldThrowInvalidAppointmentDateTimeException() {
        final Appointment appointment = AppointmentFactory.buildAppointment();

        when(appointmentRepository.getByCarPlate(appointment.getCarPlate())).thenReturn(Collections.emptyList());
        when(scheduleService.isValidDate(appointment.getDateTime())).thenReturn(true);
        when(scheduleService.isValidTime(appointment.getDateTime().toLocalTime())).thenReturn(true);
        when(scheduleService.isAvailableDateTime(appointment.getDateTime())).thenReturn(false);

        final var exceptionExpected = new InvalidAppointmentDateTimeException(
                ExceptionError.builder()
                        .description(FULL_BOOKING_APPOINTMENT_DATE_TIME_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(FULL_BOOKING_APPOINTMENT_DATE_TIME_CODE)
                                .message(FULL_BOOKING_APPOINTMENT_DATE_TIME_MESSAGE)
                                .build())
                        .build());

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentService.create(appointment));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(appointmentRepository).getByCarPlate(appointment.getCarPlate());
        verify(scheduleService).isValidDate(appointment.getDateTime());
        verify(scheduleService).isValidTime(appointment.getDateTime().toLocalTime());
        verify(scheduleService).isAvailableDateTime(appointment.getDateTime());
        verify(appointmentRepository, never()).create(appointment);
        verify(inspectionProducerService, never()).orderInspection(any());
    }

    @Test
    public void createAppointment_case5_withHasErrorOnSaveAppointment_shouldThrowAppointmentErrorException() {
        final Appointment appointment = AppointmentFactory.buildAppointment();

        when(appointmentRepository.getByCarPlate(appointment.getCarPlate())).thenReturn(Collections.emptyList());
        when(scheduleService.isValidDate(appointment.getDateTime())).thenReturn(true);
        when(scheduleService.isValidTime(appointment.getDateTime().toLocalTime())).thenReturn(true);
        when(scheduleService.isAvailableDateTime(appointment.getDateTime())).thenReturn(true);
        when(appointmentRepository.create(appointment)).thenThrow(GenericDatabaseException.class);

        final var exceptionExpected =  new AppointmentErrorException(
                ExceptionError.builder()
                        .description(CREATE_APPOINTMENT_ERROR_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(CREATE_APPOINTMENT_ERROR_CODE)
                                .message(CREATE_APPOINTMENT_ERROR_MESSAGE)
                                .build())
                        .build(), null);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentService.create(appointment));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(appointmentRepository).getByCarPlate(appointment.getCarPlate());
        verify(scheduleService).isValidDate(appointment.getDateTime());
        verify(scheduleService).isValidTime(appointment.getDateTime().toLocalTime());
        verify(scheduleService).isAvailableDateTime(appointment.getDateTime());
        verify(appointmentRepository).create(appointment);
        verify(inspectionProducerService, never()).orderInspection(any());
    }

    @Test
    public void createAppointment_case6_withHasErrorOnProduceInspectionOrder_shouldThrowOrderInspectionErrorException() {
        final Appointment appointment = AppointmentFactory.buildAppointment();

        final var exceptionExpected = new OrderInspectionErrorException(
                ExceptionError.builder()
                        .description(INSPECTION_PRODUCER_ERROR_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(INSPECTION_PRODUCER_ERROR_CODE)
                                .message(INSPECTION_PRODUCER_ERROR_MESSAGE)
                                .build())
                        .build(), null);

        when(appointmentRepository.getByCarPlate(appointment.getCarPlate())).thenReturn(Collections.emptyList());
        when(scheduleService.isValidDate(appointment.getDateTime())).thenReturn(true);
        when(scheduleService.isValidTime(appointment.getDateTime().toLocalTime())).thenReturn(true);
        when(scheduleService.isAvailableDateTime(appointment.getDateTime())).thenReturn(true);
        when(appointmentRepository.create(appointment)).thenReturn(appointment);
        doThrow(exceptionExpected).when(inspectionProducerService).orderInspection(any());

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> appointmentService.create(appointment));

        assertEquals(exceptionThrown, exceptionExpected);

        verify(appointmentRepository).getByCarPlate(appointment.getCarPlate());
        verify(scheduleService).isValidDate(appointment.getDateTime());
        verify(scheduleService).isValidTime(appointment.getDateTime().toLocalTime());
        verify(scheduleService).isAvailableDateTime(appointment.getDateTime());
        verify(appointmentRepository).create(appointment);
        verify(inspectionProducerService).orderInspection(any());
    }

}
