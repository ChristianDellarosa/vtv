package com.vtv.inspection.exception;

import com.vtv.inspection.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InvalidInspectionException extends BaseException {
    public InvalidInspectionException(ExceptionError exceptionError) {
        super(exceptionError);
    }
}
