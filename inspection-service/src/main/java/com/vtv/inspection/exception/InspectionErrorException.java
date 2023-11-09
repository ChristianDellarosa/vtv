package com.vtv.inspection.exception;

import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InspectionErrorException extends BaseException {
    public InspectionErrorException(ExceptionError exceptionError) {
        super(exceptionError);
    }

    public InspectionErrorException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
