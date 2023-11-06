package com.vtv.auth.exception.handler;


import com.vtv.auth.exception.GenericServerInternalException;
import com.vtv.auth.exception.SessionException;
import com.vtv.auth.exception.UserNotFoundException;
import com.vtv.auth.model.commons.ApiError;
import com.vtv.auth.model.commons.ApiErrorDetail;
import com.vtv.auth.model.commons.ExceptionError;
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
public class AuthExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiError(exception.getExceptionError(), request));
    }


    @ExceptionHandler(SessionException.class)
    public ResponseEntity<ApiError> handleSessionException(SessionException exception, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
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


