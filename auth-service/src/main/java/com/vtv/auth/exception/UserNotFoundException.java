package com.vtv.auth.exception;

import com.vtv.auth.domain.commons.ExceptionError;
import com.vtv.auth.exception.commons.BaseException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserNotFoundException extends BaseException {
    public UserNotFoundException(ExceptionError exceptionError) {
        super(exceptionError);
    }
}
