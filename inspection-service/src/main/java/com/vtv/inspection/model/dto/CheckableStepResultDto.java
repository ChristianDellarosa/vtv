package com.vtv.inspection.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vtv.inspection.model.domain.CheckStepStatus;
import com.vtv.inspection.model.domain.CheckableStepName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckableStepResultDto {
    private CheckableStepName name;
    private String observations;
    private Integer score;
    private CheckStepStatus status;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime dateTime;
}
