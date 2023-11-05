package com.vtv.inspection.controller;

import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.dto.InspectionRequestDto;
import com.vtv.inspection.model.dto.InspectionDto;
import com.vtv.inspection.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Inspection inspect(@Validated @RequestBody InspectionRequestDto inspectionRequestDto, @RequestHeader(name="Authorization") String sessionToken) {
        return inspectionService.inspect(sessionToken,
                InspectionRequest.builder()
                        .carPlate(inspectionRequestDto.getCarPlate())
                        .type(inspectionRequestDto.getType())
                        .build());
       //TODO: Migrar a InspectionDto
    }

    @GetMapping("/{carPlate}")
    public List<InspectionDto> getById(@Validated @PathVariable String carPlate) { //TODO: Ver por que buscar, quizas es una lista de todas las que tuvo
        final var hola = inspectionService.getByCarPlate(carPlate); //TODO: Quizas necesito devolver toda la inspeccion y es un solo Dto
        log.info(hola.toString());
        return null;
    }
}