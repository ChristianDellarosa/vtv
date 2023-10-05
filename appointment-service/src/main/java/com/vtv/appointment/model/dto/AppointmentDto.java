package com.vtv.appointment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.dto.validator.LocalDateTimeRange;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {

    @NotNull
    String carPlate;

    @Email
    @NotNull
    String clientEmail;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    //TODO: ESTO ME ESTA TRANSFORMANDO LA FECHA QUE YO MANDO a +3 HS, Porque supone que le llega UTC y como pongo buenos aires le suma 3 QUE HAGO!
    @LocalDateTimeRange(startHour = 9, endHour = 18) //TOOD: Horario que quisera
    @NotNull
    LocalDateTime dateTime; //TODO: La unica solucion que se me ocurrio fue transformarlo en un localDateTime

    @NotNull
    AppointmentType type;
}
