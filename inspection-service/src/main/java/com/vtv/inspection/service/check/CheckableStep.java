package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckableStepResult;

public interface CheckableStep { //TODO: Quizas tendría que ser un bean con la configuracion de los nombres y no un component directo
   CheckableStepResult check(String carPlate);
    //TODO: Quizas es una interfaz esto
}
