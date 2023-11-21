package com.vtv.inspection.controller;

import com.vtv.inspection.mapper.InspectionDtoMapper;
import com.vtv.inspection.mapper.InspectionRequestDtoMapper;
import com.vtv.inspection.model.dto.InspectionDto;
import com.vtv.inspection.model.dto.InspectionRequestDto;
import com.vtv.inspection.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    @ResponseStatus(HttpStatus.CREATED)
    public InspectionDto inspect(@Validated @RequestBody InspectionRequestDto inspectionRequestDto,
                                 @RequestHeader(name="Authorization") String sessionToken) {
        return InspectionDtoMapper.toDto(inspectionService.inspect(sessionToken,
                InspectionRequestDtoMapper.toDomain(inspectionRequestDto)));
    }

    @GetMapping("/{carPlate}")
    public List<InspectionDto> getById(@Validated @PathVariable String carPlate) { //TODO: Ver por que buscar, quizas es una lista de todas las que tuvo
        return inspectionService.getByCarPlate(carPlate).stream()
                .map(InspectionDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}