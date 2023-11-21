package com.vtv.inspection.service;


import com.vtv.inspection.model.domain.CheckStepStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CheckStepStatusServiceImplTest {

    @InjectMocks
    private CheckStepStatusServiceImpl checkStepStatusService;

    @Test
    public void getStatus_successfully() {
        final var score = 10;
        final CheckStepStatus expectedStatus = CheckStepStatus.SUCCESSFUL;

        final var response = checkStepStatusService.getStatus(score);

        assertEquals(expectedStatus, response);
    }

    @Test
    public void getStatus_warning() {
        final Integer score = 5;
        CheckStepStatus expectedStatus = CheckStepStatus.WARNING;

        final var response = checkStepStatusService.getStatus(score);

        assertEquals(expectedStatus, response);
    }

    @Test
    public void getStatus_dangerous() {
        final Integer score = 1;
        final CheckStepStatus expectedStatus = CheckStepStatus.DANGEROUS;

        final var response = checkStepStatusService.getStatus(score);

        assertEquals(expectedStatus, response);
    }
}