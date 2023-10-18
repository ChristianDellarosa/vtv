package com.vtv.inspection.controller;

import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.dto.InspectionRequestDto;
import com.vtv.inspection.model.dto.InspectionResultDto;
import com.vtv.inspection.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inspections")
@Slf4j
public class InspectionController {

    private final InspectionService inspectionService;

    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @PostMapping
    public InspectionResultDto inspect(@Validated @RequestBody InspectionRequestDto inspectionRequestDto) {
        inspectionService.inspect(
                InspectionRequest.builder()
                        .carPlate(inspectionRequestDto.getCarPlate())
                        .type(inspectionRequestDto.getType())
                        .build());
        return null;
    }

    @GetMapping("/{carPlate}")
    public List<InspectionResultDto> getById(@Validated @RequestParam String carPlate) { //TODO: Ver por que buscar, quizas es una lista de todas las que tuvo
        final var hola = inspectionService.getByCarPlate(carPlate); //TODO: Quizas necesito devolver toda la inspeccion y es un solo Dto
        log.info(hola.toString());
        return null;
    }
}