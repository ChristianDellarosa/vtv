package com.vtv.inspection.model.dto;

import com.vtv.inspection.model.domain.AppointmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionRequestDto {
    private String carPlate;
    private AppointmentType type;
}
