package com.vtv.appointment.exception;

import com.vtv.appointment.exception.commons.BaseException;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ScheduleErrorException extends BaseException {
    public ScheduleErrorException(ExceptionError exceptionError, Throwable cause) {
        super(exceptionError, cause);
    }
}
