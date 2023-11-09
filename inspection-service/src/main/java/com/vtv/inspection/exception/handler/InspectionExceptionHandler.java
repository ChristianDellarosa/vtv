package com.vtv.inspection.exception.handler;


import com.vtv.inspection.exception.GenericServerInternalException;
import com.vtv.inspection.exception.OrderInspectionStrategyNotExistsException;
import com.vtv.inspection.model.domain.commons.ApiError;
import com.vtv.inspection.model.domain.commons.ApiErrorDetail;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class InspectionExceptionHandler {

    @ExceptionHandler(OrderInspectionStrategyNotExistsException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(OrderInspectionStrategyNotExistsException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    @ExceptionHandler(GenericServerInternalException.class)
    public ResponseEntity<ApiError> handleGenericServerInternalException(GenericServerInternalException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
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

