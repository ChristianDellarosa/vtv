package com.vtv.inspection.service.check;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public abstract class CheckableStep {
    private String name; //TODO: O tener un getName o implementar un nombre de tipo

    public abstract CheckableStepResult check();
}
