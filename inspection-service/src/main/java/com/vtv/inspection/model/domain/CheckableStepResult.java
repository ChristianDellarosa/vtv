package com.vtv.inspection.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckableStepResult {
    private CheckableStepName name;
    private String observations;
    private Integer score;
    private CheckStepStatus status;
    private ZonedDateTime dateTime; //TODO: Para que esta este campo?
    //TODO: Falta createAt
}
