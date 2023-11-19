package com.vtv.inspection.controller;

import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.dto.InspectionDto;
import com.vtv.inspection.model.dto.InspectionRequestDto;
import com.vtv.inspection.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/inspections")
@Slf4j
public class InspectionController {

    private final InspectionService inspectionService;

    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @PostMapping
    public InspectionDto inspect(@Validated @RequestBody InspectionRequestDto inspectionRequestDto, @RequestHeader(name="Authorization") String sessionToken) {
        final var inspection = inspectionService.inspect(sessionToken,
                InspectionRequest.builder()
                        .carPlate(inspectionRequestDto.getCarPlate())
                        .type(inspectionRequestDto.getType())
                        .build());

        return InspectionDto.builder() //TODO: mappers everywhere
                .id(inspection.getId())
                .carPlate(inspection.getCarPlate())
                .result(inspection.getResult())
                .score(inspection.getScore())
                .appointmentType(inspection.getAppointmentType())
                .status(inspection.getStatus())
                .dateTime(inspection.getDateTime())
                .clientEmail(inspection.getClientEmail())
                .build();
    }

    @GetMapping("/{carPlate}")
    public List<InspectionDto> getById(@Validated @PathVariable String carPlate) { //TODO: Ver por que buscar, quizas es una lista de todas las que tuvo
        return inspectionService.getByCarPlate(carPlate).stream()
                .map(inspection ->
                     InspectionDto.builder()
                            .id(inspection.getId())
                            .carPlate(inspection.getCarPlate())
                            .result(inspection.getResult())
                            .score(inspection.getScore())
                            .appointmentType(inspection.getAppointmentType())
                            .status(inspection.getStatus())
                            .dateTime(inspection.getDateTime())
                            .clientEmail(inspection.getClientEmail())
                            .build()
                ).collect(Collectors.toList());
    }
}