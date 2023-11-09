package com.vtv.inspection.service;

import com.vtv.inspection.client.AuthClient;
import com.vtv.inspection.exception.GenericDatabaseException;
import com.vtv.inspection.exception.commons.GenericServerInternalException;
import com.vtv.inspection.exception.commons.GenericUnauthorizedException;
import com.vtv.inspection.exception.InspectionErrorException;
import com.vtv.inspection.exception.InvalidInspectionException;
import com.vtv.inspection.exception.UnauthorizedUserException;
import com.vtv.inspection.model.domain.CheckableStepResult;
import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.domain.InspectionResult;
import com.vtv.inspection.model.domain.InspectionStatus;
import com.vtv.inspection.model.domain.commons.ErrorDetail;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import com.vtv.inspection.repository.InspectionRepository;
import com.vtv.inspection.service.check.CheckableStep;
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

    private static final String UNAUTHORIZED_USER_DESCRIPTION = "The user is not authorized to perform an inspection, please sign in to auth service";
    private static final String VALIDATE_SESSION_ERROR_DESCRIPTION = "An error occurred while validating session user";

    private static final String INVALID_INSPECTION_DESCRIPTION = "The inspection entered is not valid either because it does not belong to today or because it does not exist";
    private static final Integer INVALID_INSPECTION_DESCRIPTION_CODE = 310;
    private static final String INVALID_INSPECTION_DESCRIPTION_MESSAGE = "The inspection entered is not valid";

    public InspectionServiceImpl(List<CheckableStep> checkableSteps, InspectionRepository inspectionRepository, AuthClient authClient) {
        this.checkableSteps = checkableSteps;
        this.inspectionRepository = inspectionRepository;
        this.authClient = authClient;
    }

    @Override
    public Inspection inspect(String sessionToken, InspectionRequest inspectionRequest) {

        validateSession(sessionToken);

        //TODO: Podriamos separar inspeccion de inspection order
        final Inspection inspection = getInspection(inspectionRequest);

        final InspectionResult result = inspectCar(inspection);

        inspection.setResult(result);
        inspection.setScore(result.calculateScore());
        inspection.setStatus(result.calculateStatus());

        saveInspection(inspection);

        return inspection;
    }

    private InspectionResult inspectCar(Inspection inspection) {
        final List<CheckableStepResult> checkableStepResults = checkableSteps.stream()
                .map(checkableStep -> checkableStep.check(inspection.getCarPlate()))
                .toList();

        return InspectionResult.builder()
                .checkableStepResults(checkableStepResults)
                .build();
    }

    private Inspection getInspection(InspectionRequest inspectionRequest) {
        final List<Inspection> inspections = getInspectionsByCarPlateAndAppointmentType(inspectionRequest);

        //TODO: Solo reparar si la fecha es la de hoy, no se pueden reparar autos de dias anteriores ni posteriores, solos del dia de la fecha
        return inspections
                .stream()
                .filter(insp -> InspectionStatus.PENDING.equals(insp.getStatus())
                        && insp.getDateTime().getDayOfYear() == ZonedDateTime.now().getDayOfYear()) //TODO: Que sea del dia de hoy y que este pendiente
                .min((Comparator.comparing(Inspection::getDateTime))) //TODO: Ver si esto tiene sentido
                .orElseThrow(() -> {
                    log.info(INVALID_INSPECTION_DESCRIPTION);
                    return new InvalidInspectionException(
                            ExceptionError.builder()
                                    .description(INVALID_INSPECTION_DESCRIPTION)
                                    .errorDetail(ErrorDetail.builder()
                                            .code(INVALID_INSPECTION_DESCRIPTION_CODE)
                                            .message(INVALID_INSPECTION_DESCRIPTION_MESSAGE)
                                            .build())
                                    .build());
                });
    }

    private List<Inspection> getInspectionsByCarPlateAndAppointmentType(InspectionRequest inspectionRequest) {
        List<Inspection> inspections;
        try {
            inspections = inspectionRepository.getByCarPlateAndAppointmentType(inspectionRequest.getCarPlate(), inspectionRequest.getType());
        } catch (GenericDatabaseException genericDatabaseException) {
            throw new InspectionErrorException( //TODO: Quizas tiene sentido otra excepcion
                    ExceptionError.builder()
                            .description(genericDatabaseException.getExceptionError().description())
                            .errorDetail(ErrorDetail.builder()
                                    .code(genericDatabaseException.getExceptionError().errorDetail().code())
                                    .message(genericDatabaseException.getExceptionError().errorDetail().message())
                                    .build())
                            .build(), genericDatabaseException.getCause());
        }
        return inspections;
    }


    @Override
    public List<Inspection> getByCarPlate(String carPlate) {
        try {
            return inspectionRepository.getByCarPlate(carPlate);
        } catch (GenericDatabaseException genericDatabaseException) {
            throw new InspectionErrorException( //TODO: Quizas tiene sentido otra excepcion
                    ExceptionError.builder()
                            .description(genericDatabaseException.getExceptionError().description())
                            .errorDetail(ErrorDetail.builder()
                                    .code(genericDatabaseException.getExceptionError().errorDetail().code())
                                    .message(genericDatabaseException.getExceptionError().errorDetail().message())
                                    .build())
                            .build(), genericDatabaseException.getCause());
        }

    }

    private void saveInspection(Inspection inspection) {
        try {
            inspectionRepository.save(inspection);
        } catch (GenericDatabaseException genericDatabaseException) {
            throw new InspectionErrorException(
                    ExceptionError.builder()
                            .description(genericDatabaseException.getExceptionError().description())
                            .errorDetail(ErrorDetail.builder()
                                    .code(genericDatabaseException.getExceptionError().errorDetail().code())
                                    .message(genericDatabaseException.getExceptionError().errorDetail().message())
                                    .build())
                            .build(), genericDatabaseException.getCause());
        }
    }


    private void validateSession(String sessionToken) {
        try {
            this.authClient.validateSession(sessionToken); // TODO: A futuro deberia ser un filtro, interceptor o algo de ese estilo
        } catch (GenericUnauthorizedException unauthorizedException) {
            log.info(UNAUTHORIZED_USER_DESCRIPTION, unauthorizedException);
            throw new UnauthorizedUserException(
                    ExceptionError.builder()
                            .description(UNAUTHORIZED_USER_DESCRIPTION)
                            .errorDetail(ErrorDetail.builder()
                                    .code(unauthorizedException.getExceptionError().errorDetail().code())
                                    .message(unauthorizedException.getExceptionError().errorDetail().message())
                                    .build())
                            .build());
        } catch (GenericServerInternalException serverInternalException) {
            log.error(VALIDATE_SESSION_ERROR_DESCRIPTION, serverInternalException);
            throw new InspectionErrorException(
                    ExceptionError.builder()
                            .description(VALIDATE_SESSION_ERROR_DESCRIPTION)
                            .errorDetail(ErrorDetail.builder()
                                    .code(serverInternalException.getExceptionError().errorDetail().code())
                                    .message(serverInternalException.getExceptionError().errorDetail().message())
                                    .build())
                            .build(), serverInternalException);
        }

    }
}
