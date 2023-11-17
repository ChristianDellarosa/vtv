package com.vtv.appointment.exception;

import com.vtv.appointment.exception.commons.BaseException;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ScheduleFilterException extends BaseException {
    public ScheduleFilterException(ExceptionError exceptionError) {
        super(exceptionError);
    }
}
