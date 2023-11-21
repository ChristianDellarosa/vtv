package com.vtv.inspection.service.check;


import com.vtv.inspection.model.domain.CheckStepStatus;
import com.vtv.inspection.model.domain.CheckableStepName;
import com.vtv.inspection.model.domain.CheckableStepResult;
import com.vtv.inspection.service.CheckStepScoreService;
import com.vtv.inspection.service.CheckStepStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.vtv.inspection.service.check.TyreCheckStep.DANGEROUS_OBSERVATIONS;
import static com.vtv.inspection.service.check.TyreCheckStep.WARNING_OBSERVATIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TyreCheckStepTest {
    private final static CheckableStepName CHECKABLE_STEP_NAME = CheckableStepName.TYRE;
    private final static String CAR_PLATE = "KPK123";
    @Mock
    private CheckStepScoreService checkStepScoreService;
    @Mock
    private CheckStepStatusService checkStepStatusService;
    @InjectMocks
    private TyreCheckStep tyreCheckStep;

    @Test
    void shouldCheckSuccessfullyWith_SuccessfullyStatus() {
        final var dangerousScore = 10;
        final var checkStepStatusExpected = CheckStepStatus.SUCCESSFUL;

        when(checkStepScoreService.getScore(CHECKABLE_STEP_NAME))
                .thenReturn(dangerousScore);
        when(checkStepStatusService.getStatus(dangerousScore))
                .thenReturn(checkStepStatusExpected);

        final CheckableStepResult checkableStepResult = tyreCheckStep.check(CAR_PLATE);

        assertEquals(CHECKABLE_STEP_NAME, checkableStepResult.getName());
        assertEquals(dangerousScore, checkableStepResult.getScore());
        assertEquals(checkStepStatusExpected, checkableStepResult.getStatus());
        assertNull(checkableStepResult.getObservations());

        verify(checkStepScoreService).getScore(CHECKABLE_STEP_NAME);
        verify(checkStepStatusService).getStatus(dangerousScore);
    }

    @Test
    void shouldCheckSuccessfullyWith_DangerousStatus() {
        final var dangerousScore = 1;
        final var checkStepStatusExpected = CheckStepStatus.DANGEROUS;

        when(checkStepScoreService.getScore(CHECKABLE_STEP_NAME))
                .thenReturn(dangerousScore);
        when(checkStepStatusService.getStatus(dangerousScore))
                .thenReturn(checkStepStatusExpected);

        final CheckableStepResult checkableStepResult = tyreCheckStep.check(CAR_PLATE);

        assertEquals(CHECKABLE_STEP_NAME, checkableStepResult.getName());
        assertEquals(dangerousScore, checkableStepResult.getScore());
        assertEquals(checkStepStatusExpected, checkableStepResult.getStatus());
        assertEquals(DANGEROUS_OBSERVATIONS, checkableStepResult.getObservations());

        verify(checkStepScoreService).getScore(CHECKABLE_STEP_NAME);
        verify(checkStepStatusService).getStatus(dangerousScore);
    }

    @Test
    void shouldCheckSuccessfullyWith_WarningStatus() {
        final var dangerousScore = 8;
        final var checkStepStatusExpected = CheckStepStatus.WARNING;

        when(checkStepScoreService.getScore(CHECKABLE_STEP_NAME))
                .thenReturn(dangerousScore);
        when(checkStepStatusService.getStatus(dangerousScore))
                .thenReturn(checkStepStatusExpected);

        final CheckableStepResult checkableStepResult = tyreCheckStep.check(CAR_PLATE);

        assertEquals(CHECKABLE_STEP_NAME, checkableStepResult.getName());
        assertEquals(dangerousScore, checkableStepResult.getScore());
        assertEquals(checkStepStatusExpected, checkableStepResult.getStatus());
        assertEquals(WARNING_OBSERVATIONS, checkableStepResult.getObservations());

        verify(checkStepScoreService).getScore(CHECKABLE_STEP_NAME);
        verify(checkStepStatusService).getStatus(dangerousScore);
    }
}
