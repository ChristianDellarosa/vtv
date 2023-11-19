package com.vtv.inspection.model.domain;

public enum CheckStepStatus {
    SUCCESSFUL,
    WARNING,
    DANGEROUS;

    public static Boolean isSuccessfulStatus(CheckStepStatus status) {
        return SUCCESSFUL.equals(status);
    }

    public static Boolean isWarningStatus(CheckStepStatus status) {
        return WARNING.equals(status);
    }

    public static Boolean isDangerousStatus(CheckStepStatus status) {
        return DANGEROUS.equals(status);
    }
}
