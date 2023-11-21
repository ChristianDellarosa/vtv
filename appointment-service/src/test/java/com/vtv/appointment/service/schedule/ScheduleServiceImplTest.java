package com.vtv.appointment.service.schedule;

import com.vtv.appointment.configuration.ScheduleConfiguration;
import com.vtv.appointment.exception.ScheduleErrorException;
import com.vtv.appointment.exception.ScheduleFilterException;
import com.vtv.appointment.exception.commons.GenericDatabaseException;
import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import com.vtv.appointment.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static com.vtv.appointment.service.schedule.ScheduleServiceImpl.FILTER_COMBINATION_NOT_EXISTS_MESSAGE;
import static com.vtv.appointment.service.schedule.ScheduleServiceImpl.FILTER_COMBINATION_NOT_EXISTS_NOT_EXISTS_CODE;
import static com.vtv.appointment.service.schedule.ScheduleServiceImpl.GET_SCHEDULE_ERROR_CODE;
import static com.vtv.appointment.service.schedule.ScheduleServiceImpl.GET_SCHEDULE_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ScheduleConfiguration scheduleConfiguration;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    public static final Integer ONE_APPOINTMENT_FOR_HOUR = 1;

    @Test
    public void get_with_disableMonth() {


        final ScheduleQuery scheduleQuery = ScheduleQuery.builder()
                .month(Month.APRIL)
                .build();

        when(scheduleConfiguration.getPerHour()).thenReturn(ONE_APPOINTMENT_FOR_HOUR);
        when(scheduleConfiguration.getMonthsEnable()).thenReturn(List.of(Month.NOVEMBER));
        when(scheduleConfiguration.getDaysEnable()).thenReturn(List.of(DayOfWeek.FRIDAY));

        when(appointmentRepository.getByDateTimeRange(any(), any())).thenReturn(Collections.emptyList());

        final List<ZonedDateTime> response = scheduleService.get(scheduleQuery);

        assertEquals(response, Collections.emptyList());

        verify(appointmentRepository).getByDateTimeRange(any(), any());
    }

    @Test
    public void get_with_disable_day() {
        final ScheduleQuery scheduleQuery = ScheduleQuery.builder()
                .month(Month.DECEMBER)
                .dayNumber(4) //Monday
                .build();

        when(scheduleConfiguration.getPerHour()).thenReturn(ONE_APPOINTMENT_FOR_HOUR);

        when(scheduleConfiguration.getDaysEnable()).thenReturn(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));

        when(appointmentRepository.getByDateTimeRange(any(), any())).thenReturn(Collections.emptyList());

        final List<ZonedDateTime> response = scheduleService.get(scheduleQuery);

        assertEquals(response, Collections.emptyList());

        verify(appointmentRepository).getByDateTimeRange(any(), any());
    }

    @Test
    public void get_with_disable_hour() {
        final ScheduleQuery scheduleQuery = ScheduleQuery.builder()
                .month(Month.DECEMBER)
                .dayNumber(4)
                .hour(11)
                .build();

        when(scheduleConfiguration.getPerHour()).thenReturn(ONE_APPOINTMENT_FOR_HOUR);

        when(scheduleConfiguration.getDaysEnable()).thenReturn(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));

        when(appointmentRepository.getByDateTimeRange(any(), any())).thenReturn(Collections.emptyList());

        final List<ZonedDateTime> response = scheduleService.get(scheduleQuery);

        assertEquals(response, Collections.emptyList());

        verify(appointmentRepository).getByDateTimeRange(any(), any());
    }

    @Test
    public void get_with_non_filters() {
        final ScheduleQuery scheduleQuery = ScheduleQuery.builder()
                .build();

        when(scheduleConfiguration.getPerHour()).thenReturn(ONE_APPOINTMENT_FOR_HOUR);

        when(scheduleConfiguration.getDaysEnable()).thenReturn(List.of(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));

        when(appointmentRepository.getByDateTimeRange(any(), any())).thenReturn(Collections.emptyList());

        final List<ZonedDateTime> response = scheduleService.get(scheduleQuery);

        assertEquals(response, Collections.emptyList());

        verify(appointmentRepository).getByDateTimeRange(any(), any());
    }

    @Test
    public void get_withDatabaseError_throw_ScheduleErrorException() {
        doThrow(GenericDatabaseException.class).
                when(appointmentRepository).getByDateTimeRange(any(), any());

        ScheduleQuery scheduleQuery = ScheduleQuery.builder()
                .month(Month.NOVEMBER).build();

        final var exceptionExpected = new ScheduleErrorException(
                ExceptionError.builder()
                        .description(GET_SCHEDULE_ERROR_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .message(GET_SCHEDULE_ERROR_MESSAGE)
                                .code(GET_SCHEDULE_ERROR_CODE)
                                .build())
                        .build(), null);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> scheduleService.get(scheduleQuery));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());
    }

     @Test
    public void getWithInvalidFilterCombination() {
        final var badDayForNovember = 31;
         final ScheduleQuery scheduleQuery = ScheduleQuery.builder()
                 .month(Month.NOVEMBER)
                 .dayNumber(badDayForNovember)
                 .build();

         final var exceptionExpected = new ScheduleFilterException(
                 ExceptionError.builder()
                         .description(FILTER_COMBINATION_NOT_EXISTS_MESSAGE)
                         .errorDetail(ErrorDetail.builder()
                                 .message(FILTER_COMBINATION_NOT_EXISTS_MESSAGE)
                                 .code(FILTER_COMBINATION_NOT_EXISTS_NOT_EXISTS_CODE)
                                 .build())
                         .build());

         final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                 () -> scheduleService.get(scheduleQuery));

         assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
         assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
         assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
         assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());
    }
}