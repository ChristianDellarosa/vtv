package com.vtv.inspection.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtv.inspection.exception.GenericServerInternalException;
import com.vtv.inspection.exception.GenericUnauthorizedException;
import com.vtv.inspection.model.domain.commons.ApiError;
import com.vtv.inspection.model.domain.commons.ErrorDetail;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class AuthClientInterceptor implements ClientHttpRequestInterceptor { //TODO: add Test for interceptor
    private final ObjectMapper mapper;

    private static final String ORDER_INSPECTION_STRATEGY_NOT_EXISTS_DESCRIPTION = "The strategy to process inspection order does not exist";
    private static final Integer ORDER_INSPECTION_STRATEGY_NOT_EXISTS_CODE = 300;
    private static final String ORDER_INSPECTION_STRATEGY_NOT_EXISTS_MESSAGE = "The order Inspection strategy not exists";

    public AuthClientInterceptor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        final var httpResponse = execution.execute(request, body);


        if(httpResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
            final ApiError apiError = mapToApiError(httpResponse);

            throw new GenericUnauthorizedException(
                    ExceptionError.builder()
                            .description(apiError.getDescription())
                            .errorDetail(ErrorDetail.builder()
                                    .code(apiError.getApiErrorDetail().code())
                                    .message(apiError.getApiErrorDetail().message())
                                    .build())
                            .build());
        }
        if(httpResponse.getStatusCode().is5xxServerError()) {
            throw new GenericServerInternalException( //TODO: En realidad esto debería ser un error no manejado,
                    ExceptionError.builder()
                            .description("internal error")
                            .errorDetail(ErrorDetail.builder()
                                    .code(30)
                                    .message("ver mensaje de error stactrace?")
                                    .build())
                            .build());
        }

        return httpResponse;
    }

    private ApiError mapToApiError(ClientHttpResponse httpResponse) {
        try {
            String result = IOUtils.toString(httpResponse.getBody(), String.valueOf(StandardCharsets.UTF_8));

            if (!result.isEmpty()) {
                return mapper.readValue(result, ApiError.class);
            }

            log.info("Response body is empty {}", result);
            throw new GenericServerInternalException( //TODO: En realidad esto debería ser un error no manejado,
                    ExceptionError.builder()
                            .description("Unknown Error") //TODO: Ver
                            .errorDetail(ErrorDetail.builder()
                                    .code(100)
                                    .message("Unknown Error")
                                    .build())
                            .build());

        } catch (IOException exception) {
            log.error("An unexpected error has occurred reading ClientHttpResponse object.", exception);
            throw new GenericServerInternalException(
                    ExceptionError.builder()
                            .description(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_DESCRIPTION)
                            .errorDetail(ErrorDetail.builder()
                                    .code(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_CODE)
                                    .message(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_MESSAGE)
                                    .build())
                            .build(), exception);
        }
    }
}
