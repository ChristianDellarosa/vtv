package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.domain.InspectionResponse;
import com.vtv.inspection.model.domain.InspectionStatus;
import com.vtv.inspection.repository.InspectionRepository;
import com.vtv.inspection.service.check.CheckableStep;
import com.vtv.inspection.service.check.CheckableStepResult;
import com.vtv.inspection.service.check.InspectionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

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

        //TODO: Podriamos separar inspeccion de inspection order
        final var inspections = inspectionRepository.getByCarPlateAndAppointmentType(inspectionRequest.getCarPlate(), inspectionRequest.getType());

        //TODO: Solo reparar si la fecha es la de hoy, no se pueden reparar autos de dias anteriores ni posteriores, solos del dia de la fecha
        final Inspection inspectionOrder = inspections
                .stream()
                .filter(inspection -> InspectionStatus.PENDING.equals(inspection.getStatus())
                        && inspection.getDateTime().getDayOfYear() == ZonedDateTime.now().getDayOfYear()) //TODO: Que sea del dia de hoy y que este pendiente
                .min((Comparator.comparing(Inspection::getDateTime))) //TODO: Ver si esto tiene sentido
                .orElseThrow(() -> new RuntimeException("NOT FOUND"));

        final List<CheckableStepResult> checkableStepResults = checkableSteps.stream()
                .map(checkableStep -> checkableStep.check(inspectionOrder.getCarPlate()))
                .toList();

        final var result = InspectionResult.builder()
                .results(checkableStepResults)
                .build();

        //TODO: Quedo raaaro
        inspectionOrder.setResult(result);
        inspectionOrder.setScore(result.calculateScore());
        inspectionOrder.setStatus(result.getStatus());

        inspectionRepository.save(inspectionOrder);

   /*     final var inspection = inspectionRepository.save(Inspection.
                builder()
                .appointmentType(inspectionOrder.getAppointmentType())
                .clientEmail(inspectionOrder.getClientEmail())
                .dateTime(inspectionOrder.getDateTime())
                .status(result.getStatus())
                .score(result.calculateScore())
                .result(result)
                .build() //TODO: Ver que devolver
        );*/


        //TODO: Delete order, o hacer borrado logico

        return null;

    }

    @Override
    public InspectionResponse getByCarPlate(String carPlate) {
        return null;
    }
}
