package com.vtv.inspection.exception.handler;


import com.vtv.inspection.exception.*;
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
public class InspectionExceptionHandler {

    @ExceptionHandler(InvalidInspectionException.class)
    public ResponseEntity<ApiError> handleInvalidInspectionException(InvalidInspectionException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ApiError> handleUnauthorizedUserException(UnauthorizedUserException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    @ExceptionHandler(InspectionErrorException.class)
    public ResponseEntity<ApiError> handleInspectionErrorException(InspectionErrorException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    @ExceptionHandler(OrderInspectionStrategyNotExistsException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(OrderInspectionStrategyNotExistsException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }

    //    @ExceptionHandler(GenericUnauthorizedException.class)
//    public ResponseEntity<ApiError> handleGenericUnauthorizedException(GenericUnauthorizedException exception, WebRequest request) {
//
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(buildApiError(exception.getExceptionError(), request));
//    }

//    @ExceptionHandler(GenericServerInternalException.class) //TODO: Quizas no deberia estar, porque no es de negocio, nadie deberia lanzar esta excepcion?
//    public ResponseEntity<ApiError> handleGenericServerInternalException(GenericServerInternalException exception, WebRequest request) {
//
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(buildApiError(exception.getExceptionError(), request));
//    }

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

//TODO: AGREGAR EXCEPCION DE VALIDACIONES DE JAKARTA

