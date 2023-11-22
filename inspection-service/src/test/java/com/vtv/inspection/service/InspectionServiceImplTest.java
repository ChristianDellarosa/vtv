package com.vtv.inspection.service;

import com.vtv.inspection.client.AuthClient;
import com.vtv.inspection.exception.InspectionErrorException;
import com.vtv.inspection.exception.InvalidInspectionException;
import com.vtv.inspection.exception.UnauthorizedUserException;
import com.vtv.inspection.exception.commons.GenericDatabaseException;
import com.vtv.inspection.exception.commons.GenericUnauthorizedException;
import com.vtv.inspection.mock.InspectionFactory;
import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.domain.commons.ErrorDetail;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import com.vtv.inspection.repository.InspectionRepository;
import com.vtv.inspection.service.check.CheckableStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static com.vtv.inspection.service.InspectionServiceImpl.INVALID_INSPECTION_DESCRIPTION;
import static com.vtv.inspection.service.InspectionServiceImpl.INVALID_INSPECTION_DESCRIPTION_CODE;
import static com.vtv.inspection.service.InspectionServiceImpl.INVALID_INSPECTION_DESCRIPTION_MESSAGE;
import static com.vtv.inspection.service.InspectionServiceImpl.UNAUTHORIZED_USER_DESCRIPTION;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InspectionServiceImplTest {
    @Mock
    private List<CheckableStep> checkableSteps;

    @Mock
    private InspectionRepository inspectionRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private InspectionServiceImpl inspectionService;

    @Test
    public void inspect_validInspectionRequest_shouldReturnInspectionWithResult() {
        final String sessionToken = "valid_session_token";
        final InspectionRequest inspectionRequest = new InspectionRequest("ABC123", AppointmentType.INSPECTION);
        final Inspection inspection = InspectionFactory.buildInspection(ZonedDateTime.now());

        doNothing().when(authClient).validateSession(sessionToken);
        when(inspectionRepository.getByCarPlateAndAppointmentType(
                inspectionRequest.getCarPlate(),
                inspectionRequest.getType()))
                .thenReturn(Collections.singletonList(inspection));

        final Inspection response = inspectionService.inspect(sessionToken, inspectionRequest);

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getScore()).isNotNull();

        verify(authClient).validateSession(sessionToken);
        verify(inspectionRepository).getByCarPlateAndAppointmentType(inspectionRequest.getCarPlate(), inspectionRequest.getType());
    }

    @Test
    public void inspect_invalidSessionToken_shouldThrowUnauthorizedUserException() {
        final String sessionToken = "invalid_session_token";
        final var errorCode = 2;
        final var errorMessage = "ErrorMessage";

        final InspectionRequest inspectionRequest = new InspectionRequest("ABC123", AppointmentType.INSPECTION);

        final var exceptionExpected = new UnauthorizedUserException(
                ExceptionError.builder()
                        .description(UNAUTHORIZED_USER_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .code(errorCode)
                                .message(errorMessage)
                                .build())
                        .build());

        final var clientException = new GenericUnauthorizedException(
                ExceptionError.builder()
                        .description(errorMessage)
                        .errorDetail(ErrorDetail.builder()
                                .code(errorCode)
                                .message(errorMessage)
                                .build())
                        .build());

        doThrow(clientException)
                .when(authClient).validateSession(sessionToken);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> inspectionService.inspect(sessionToken, inspectionRequest));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());

        verify(authClient).validateSession(sessionToken);
    }

    @Test
    public void inspect_invalidInspectionRequest_shouldThrowInvalidInspectionException() {
        final String sessionToken = "valid_session_token";
        final InspectionRequest inspectionRequest = new InspectionRequest("ABC123", AppointmentType.INSPECTION);

        final var exceptionExpected = new InvalidInspectionException(
                ExceptionError.builder()
                        .description(INVALID_INSPECTION_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .code(INVALID_INSPECTION_DESCRIPTION_CODE)
                                .message(INVALID_INSPECTION_DESCRIPTION_MESSAGE)
                                .build())
                        .build());

        doNothing().when(authClient).validateSession(sessionToken);

        when(inspectionRepository.getByCarPlateAndAppointmentType(inspectionRequest.getCarPlate(), inspectionRequest.getType()))
                .thenReturn(Collections.emptyList());

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> inspectionService.inspect(sessionToken, inspectionRequest));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(authClient).validateSession(sessionToken);
        verify(inspectionRepository).getByCarPlateAndAppointmentType(inspectionRequest.getCarPlate(), inspectionRequest.getType());
    }

    @Test
    public void getByCarPlate_validCarPlate_shouldReturnListInspection() {
        final String carPlate = "ABC123";
        final var inspection = InspectionFactory.buildInspection();

        when(inspectionRepository.getByCarPlate(carPlate))
                .thenReturn(Collections.singletonList(inspection));

        final List<Inspection> result = inspectionService.getByCarPlate(carPlate);

        assertThat(result).isNotNull();
        assertThat(result.get(0).getCarPlate()).isEqualTo(carPlate);

        verify(inspectionRepository).getByCarPlate(carPlate);
    }

    @Test
    public void getByCarPlate_invalidCarPlate_shouldThrowGenericDatabaseException() {
        // Arrange
        String carPlate = "invalid_car_plate";
        final var errorCode = 2;
        final var errorMessage = "ErrorMessage";

        final var genericDatabaseException = new GenericDatabaseException(
                ExceptionError.builder()
                        .description(UNAUTHORIZED_USER_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .code(errorCode)
                                .message(errorMessage)
                                .build())
                        .build(), null);

        final var exceptionExpected = new InspectionErrorException(
                ExceptionError.builder()
                        .description(genericDatabaseException.getExceptionError().description())
                        .errorDetail(ErrorDetail.builder()
                                .code(genericDatabaseException.getExceptionError().errorDetail().code())
                                .message(genericDatabaseException.getExceptionError().errorDetail().message())
                                .build())
                        .build(), genericDatabaseException.getCause());

        when(inspectionRepository.getByCarPlate(carPlate))
                .thenThrow(genericDatabaseException);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () -> inspectionService.getByCarPlate(carPlate));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(inspectionRepository).getByCarPlate(carPlate);
    }
}
