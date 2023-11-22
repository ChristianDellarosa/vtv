package com.vtv.appointment.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.dto.validator.DateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {

    @NotNull
    @Pattern(regexp = "^([A-Z]{2})([0-9]{3})([A-Z]{2})|([A-Z]{3})([0-9]{3})$", message = "The carPlate format is invalid")
    private String carPlate;

    @Email
    @NotNull
    private String clientEmail;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTime
    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private AppointmentType type;
}
