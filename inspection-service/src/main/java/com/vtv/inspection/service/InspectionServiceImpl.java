package com.vtv.inspection.service;

import com.vtv.inspection.client.AuthClient;
import com.vtv.inspection.model.domain.*;
import com.vtv.inspection.repository.InspectionRepository;
import com.vtv.inspection.service.check.CheckableStep;
import jakarta.websocket.SessionException;
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

    private final AuthClient authClient;

    public InspectionServiceImpl(List<CheckableStep> checkableSteps, InspectionRepository inspectionRepository, AuthClient authClient) {
        this.checkableSteps = checkableSteps;
        this.inspectionRepository = inspectionRepository;
        this.authClient = authClient;
    }

    @Override
    public Inspection inspect(String sessionToken, InspectionRequest inspectionRequest) {



        //TODO: Podriamos separar inspeccion de inspection order
        final var inspections = inspectionRepository.getByCarPlateAndAppointmentType(inspectionRequest.getCarPlate(), inspectionRequest.getType());

        //TODO: Solo reparar si la fecha es la de hoy, no se pueden reparar autos de dias anteriores ni posteriores, solos del dia de la fecha
        final Inspection inspection = inspections
                .stream()
                .filter(insp -> InspectionStatus.PENDING.equals(insp.getStatus())
                        && insp.getDateTime().getDayOfYear() == ZonedDateTime.now().getDayOfYear()) //TODO: Que sea del dia de hoy y que este pendiente
                .min((Comparator.comparing(Inspection::getDateTime))) //TODO: Ver si esto tiene sentido
                .orElseThrow(() -> new RuntimeException("NOT FOUND"));

        final List<CheckableStepResult> checkableStepResults = checkableSteps.stream()
                .map(checkableStep -> checkableStep.check(inspection.getCarPlate()))
                .toList();

        final var result = InspectionResult.builder()
                .checkableStepResults(checkableStepResults)
                .build();

        //TODO: Quedo raaaro
        inspection.setResult(result);
        inspection.setScore(result.calculateScore());
        inspection.setStatus(result.calculateStatus());
        inspectionRepository.save(inspection);
        //TODO: Delete order, o hacer borrado logico

        return inspection;
    }

    @Override
    public Inspection getByCarPlate(String carPlate) {
        return null;
    } //TODO: Completar


    private void validateSession(String sessionToken) {
        try {
            this.authClient.validateSession(sessionToken); // TODO: A futuro deberia ser un filtro, interceptor o algo de ese estilo
        } catch (Exception exception) {
            throw new RuntimeException(""); //TODO: Armar excepcioens de negocio
        }

    }
}
