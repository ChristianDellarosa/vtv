package com.vtv.auth.exception;

import com.vtv.auth.domain.commons.ExceptionError;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserNotFoundException extends BaseException {
    public UserNotFoundException(ExceptionError exceptionError) {
        super(exceptionError);
    } //TODO: A futuro heredar de los codigos comunes
}
