package com.vtv.auth.exception;

import com.vtv.auth.domain.commons.ExceptionError;
import com.vtv.auth.exception.commons.BaseException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SessionException extends BaseException { //TODO: Analizar si hacemos 1 excepcion o varias que extiendan de session (Quizas a nivel monitoreo es mas accesible)
    public SessionException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
