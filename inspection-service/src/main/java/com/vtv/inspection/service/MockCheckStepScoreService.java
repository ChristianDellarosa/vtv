package com.vtv.inspection.service;

import com.vtv.inspection.configuration.CheckConfiguration;
import com.vtv.inspection.model.domain.CheckableStepName;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MockCheckStepScoreService implements CheckStepScoreService {
    private final CheckConfiguration checkConfiguration;

    private static final Integer APPROVED_SCORE = 10;

    private static final Integer REJECTED_SCORE_MIN = 1;

    private static final Integer REJECTED_SCORE_MAX = 5;

    private static final Integer REJECTED_BY_LESS_THAN_FIVE_SCORE_MAX = 4;

    private static final Integer REJECTED_CUSTOM = 4;

    private static final Integer OBSERVED_SCORE_MIN = 6;

    private static final Integer OBSERVED_SCORE_MAX = 9;
    private final Random random;

    public MockCheckStepScoreService(CheckConfiguration checkConfiguration) {
        this.checkConfiguration = checkConfiguration;
        this.random = new Random();
    }

    @Override
    public Integer getScore(CheckableStepName stepName) {
        if(CheckableStepName.BRAKING_SYSTEM.equals(stepName)) {
            return checkConfiguration.getIsRejectByLessThanFivePointsCheckCase()? getRejectedLessThanFiveScore() : checkConfiguration.getIsRejectedCheckCase()? REJECTED_CUSTOM : checkConfiguration.getIsObservedCheckCase()? getObservedScore() : APPROVED_SCORE;
        }
        return checkConfiguration.getIsRejectedCheckCase()? getRejectedScore() : checkConfiguration.getIsObservedCheckCase()? getObservedScore() : APPROVED_SCORE;
    }


    private Integer getObservedScore() {
        return random.nextInt(OBSERVED_SCORE_MAX - OBSERVED_SCORE_MIN + 1) + OBSERVED_SCORE_MIN;
    }

    private Integer getRejectedScore() {
        return random.nextInt(REJECTED_SCORE_MAX - REJECTED_SCORE_MIN + 1) + REJECTED_SCORE_MIN;
    }

    private Integer getRejectedLessThanFiveScore() {
        return random.nextInt(REJECTED_BY_LESS_THAN_FIVE_SCORE_MAX - REJECTED_SCORE_MIN + 1) + REJECTED_SCORE_MIN;
    }
}
