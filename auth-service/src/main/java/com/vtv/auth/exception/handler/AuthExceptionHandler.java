package com.vtv.auth.exception.handler;


import com.vtv.auth.domain.commons.ErrorDetail;
import com.vtv.auth.exception.commons.GenericServerInternalException;
import com.vtv.auth.exception.SessionException;
import com.vtv.auth.exception.UserNotFoundException;
import com.vtv.auth.domain.commons.ApiError;
import com.vtv.auth.domain.commons.ApiErrorDetail;
import com.vtv.auth.domain.commons.ExceptionError;
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
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String FIELD_VALIDATION_DEFAULT_MESSAGE = "Field validation error";
    private final static Integer FIELD_VALIDATION_CODE = 0;

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
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


