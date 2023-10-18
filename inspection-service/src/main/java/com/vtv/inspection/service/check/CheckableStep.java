package com.vtv.inspection.service.check;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public abstract class CheckableStep { //TODO: Quizas tendría que ser un bean con la configuracion de los nombres y no un component directo
    private String name; //TODO: O tener un getName o implementar un nombre de tipo

    public abstract CheckableStepResult check(String carPlate);
    //TODO: Quizas es una interfaz esto
}
