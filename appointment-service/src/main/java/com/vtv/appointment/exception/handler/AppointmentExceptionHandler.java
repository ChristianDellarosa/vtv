package com.vtv.appointment.exception.handler;


import com.vtv.appointment.model.domain.commons.ApiError;
import com.vtv.appointment.model.domain.commons.ApiErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class AppointmentExceptionHandler {

    //TODO: Agregar excepciones

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


