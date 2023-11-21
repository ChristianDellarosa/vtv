package com.vtv.inspection.service;

import com.vtv.inspection.configuration.CheckStepMockConfiguration;
import com.vtv.inspection.model.domain.CheckableStepName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockCheckStepScoreServiceTest {
    @Mock
    private CheckStepMockConfiguration checkStepMockConfiguration;

    @InjectMocks
    private MockCheckStepScoreService mockCheckStepScoreService;

    @Test
    public void getScore_brakingSystem_approvedCheckCase() {
        final CheckableStepName stepName = CheckableStepName.BRAKING_SYSTEM;
        final Integer expectedScore = 10;

        when(checkStepMockConfiguration.getIsRejectedCheckCase()).thenReturn(false);
        when(checkStepMockConfiguration.getIsObservedCheckCase()).thenReturn(false);
        when(checkStepMockConfiguration.getIsRejectByLessThanFivePointsCheckCase()).thenReturn(false);

        final Integer actualScore = mockCheckStepScoreService.getScore(stepName);
        assertEquals(expectedScore, actualScore);

        verify(checkStepMockConfiguration).getIsRejectByLessThanFivePointsCheckCase();
        verify(checkStepMockConfiguration).getIsObservedCheckCase();
        verify(checkStepMockConfiguration).getIsRejectedCheckCase();
        verify(checkStepMockConfiguration, never()).getIsApprovedCheckCase();

    }

    @Test
    public void getScore_brakingSystem_IsRejectByLessThanFivePointsCheckCase() {
        final CheckableStepName stepName = CheckableStepName.BRAKING_SYSTEM;
        final Integer expectedScoreMax = 4;

        when(checkStepMockConfiguration.getIsRejectByLessThanFivePointsCheckCase()).thenReturn(true);

        final Integer actualScore = mockCheckStepScoreService.getScore(stepName);
        assertTrue(actualScore.compareTo(expectedScoreMax) <= 0);

        verify(checkStepMockConfiguration).getIsRejectByLessThanFivePointsCheckCase();
        verify(checkStepMockConfiguration, never()).getIsApprovedCheckCase();
        verify(checkStepMockConfiguration, never()).getIsObservedCheckCase();
        verify(checkStepMockConfiguration, never()).getIsRejectedCheckCase();

    }

    @Test
    public void getScore_brakingSystem_getIsRejectedCheckCase() {
        final CheckableStepName stepName = CheckableStepName.BRAKING_SYSTEM;
        final Integer expectedScore = 4;

        when(checkStepMockConfiguration.getIsRejectByLessThanFivePointsCheckCase()).thenReturn(false);
        when(checkStepMockConfiguration.getIsRejectedCheckCase()).thenReturn(true);

        final Integer actualScore = mockCheckStepScoreService.getScore(stepName);
        assertEquals(expectedScore, actualScore);

        verify(checkStepMockConfiguration).getIsRejectByLessThanFivePointsCheckCase();
        verify(checkStepMockConfiguration).getIsRejectedCheckCase();
        verify(checkStepMockConfiguration, never()).getIsApprovedCheckCase();
        verify(checkStepMockConfiguration, never()).getIsObservedCheckCase();

    }

    @Test
    public void getScore_brakingSystem_getIsObservedCheckCase() {
        final CheckableStepName stepName = CheckableStepName.BRAKING_SYSTEM;
        final Integer expectedScoreMax = 9;
        final Integer expectedScoreMin = 6;
        when(checkStepMockConfiguration.getIsRejectByLessThanFivePointsCheckCase()).thenReturn(false);
        when(checkStepMockConfiguration.getIsRejectedCheckCase()).thenReturn(false);
        when(checkStepMockConfiguration.getIsObservedCheckCase()).thenReturn(true);

        final Integer actualScore = mockCheckStepScoreService.getScore(stepName);
        assertTrue(actualScore.compareTo(expectedScoreMin) >= 0);
        assertTrue(actualScore.compareTo(expectedScoreMax) <= 0);

        verify(checkStepMockConfiguration).getIsRejectByLessThanFivePointsCheckCase();
        verify(checkStepMockConfiguration).getIsRejectedCheckCase();
        verify(checkStepMockConfiguration).getIsObservedCheckCase();
        verify(checkStepMockConfiguration, never()).getIsApprovedCheckCase();
    }
}
