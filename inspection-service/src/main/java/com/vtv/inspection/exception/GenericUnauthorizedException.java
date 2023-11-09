package com.vtv.inspection.exception;

import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GenericUnauthorizedException extends BaseException {
    public GenericUnauthorizedException(ExceptionError exceptionError) {
        super(exceptionError);
    }
}
