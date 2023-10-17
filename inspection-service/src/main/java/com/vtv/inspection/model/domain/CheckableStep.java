package com.vtv.inspection.model.domain;

public abstract class CheckableStep {
    private String name; //TODO: Ver si estos campos no son del resultado
    private String observations; //TODO: Ver si estos campos no son del resultado
    private Integer score; //TODO: Ver si estos campos no son del resultado
    private CheckStepStatus status; //TODO: Ver si estos campos no son del resultado

    public abstract CheckeableStepResult check();
}
