package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.domain.InspectionResponse;
import com.vtv.inspection.repository.InspectionRepository;
import com.vtv.inspection.service.check.CheckableStep;
import com.vtv.inspection.service.check.CheckableStepResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InspectionServiceImpl implements InspectionService {
    private final List<CheckableStep> checkableSteps;

    private final InspectionRepository inspectionRepository;
    public InspectionServiceImpl(List<CheckableStep> checkableSteps, InspectionRepository inspectionRepository) {
        this.checkableSteps = checkableSteps;
        this.inspectionRepository = inspectionRepository;
    }

    @Override
    public InspectionResponse inspect(InspectionRequest inspectionRequest) {

        final var inspections = inspectionRepository.getByCarPlateAndAppointmentType(inspectionRequest.getCarPlate(), inspectionRequest.getType());
        final Inspection lastInspection = inspections
                .stream()
                .min((Comparator.comparing(Inspection::getDateTime)))
                .orElseThrow(() -> new RuntimeException("NOT FOUND"));


        final List<CheckableStepResult> hola = checkableSteps.stream()
                .map(CheckableStep::check)
                .toList();



    }

    @Override
    public InspectionResponse getByCarPlate(String carPlate) {
        return null;
    }
}
