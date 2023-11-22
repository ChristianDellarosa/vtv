package com.vtv.appointment.exception.handler;


import com.vtv.appointment.exception.*;
import com.vtv.appointment.exception.commons.BaseException;
import com.vtv.appointment.model.domain.commons.ApiError;
import com.vtv.appointment.model.domain.commons.ApiErrorDetail;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class AppointmentExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String FIELD_VALIDATION_DEFAULT_MESSAGE = "Field validation error";
    private final static Integer FIELD_VALIDATION_CODE = 0;
    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ApiError> handleAppointmentNotFoundException(AppointmentNotFoundException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    @ExceptionHandler(InvalidAppointmentDateTimeException.class)
    public ResponseEntity<ApiError> handleInvalidAppointmentDateTimeException(InvalidAppointmentDateTimeException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    @ExceptionHandler(AppointmentAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAppoitnmentAlreadyExistsException(AppointmentAlreadyExistsException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    @ExceptionHandler(ScheduleFilterException.class)
    public ResponseEntity<ApiError> handleScheduleFilterException(ScheduleFilterException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    @ExceptionHandler({ScheduleErrorException.class, OrderInspectionErrorException.class, AppointmentErrorException.class})
    public ResponseEntity<ApiError> handleScheduleErrorException(BaseException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        //TODO: FIXME: Debería ser una lista de errores
        final ExceptionError exceptionError =  ex.getBindingResult()
                .getFieldErrors().stream()
                .findFirst()
                .map(error -> {
                    final var errorMessage = "The field: " + error.getField() + "' " + error.getDefaultMessage();
                    return ExceptionError.builder()
                            .description(errorMessage)
                            .errorDetail(ErrorDetail.builder()
                                    .message(errorMessage)
                                    .code(FIELD_VALIDATION_CODE)
                                    .build());
                })
                .orElseGet(() -> ExceptionError.builder()
                        .description(FIELD_VALIDATION_DEFAULT_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .message(FIELD_VALIDATION_DEFAULT_MESSAGE)
                                .code(FIELD_VALIDATION_CODE)
                                .build()))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exceptionError, request));
    }

    private ApiError buildApiError(ExceptionError exceptionError, WebRequest request) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest) request).getRequest().getServletPath())
                .description(exceptionError.description())
                .apiErrorDetail(ApiErrorDetail.builder()
                        .code(Objects.nonNull(exceptionError.errorDetail())? exceptionError.errorDetail().code() : null)
                        .message(Objects.nonNull(exceptionError.errorDetail())? exceptionError.errorDetail().message() : null)
                        .build())
                .build();
    }
}


