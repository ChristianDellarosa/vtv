package com.vtv.inspection.model.dto;

import com.vtv.inspection.model.domain.AppointmentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionRequestDto {
    @NotNull
    @Pattern(regexp = "^([A-Z]{2})([0-9]{3})([A-Z]{2})|([A-Z]{3})([0-9]{3})$", message = "The carPlate format is invalid")
    private String carPlate;

    @NotNull
    private AppointmentType type;
}
