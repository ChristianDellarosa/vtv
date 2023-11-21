package com.vtv.inspection.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.vtv.inspection.model.domain.InspectionStatus.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InspectionResult {
    private List<CheckableStepResult> checkableStepResults;

    public Integer calculateScore() {
        return checkableStepResults.stream()
                .mapToInt(CheckableStepResult::getScore)
                .sum();
    }

    public InspectionStatus calculateStatus() {
        if (checkableStepResults.isEmpty()) {
            return PENDING;
        }

        final Integer sumatory = calculateScore();

        return isRejectedScore(sumatory) ? REJECTED :
                isApprovedScore(sumatory) ? APPROVED : APPROVED_WITH_OBSERVATIONS;

    }
    private Boolean hasAnyCheckableStepLessThanFive() {
        return checkableStepResults.stream().anyMatch(checkableStepResult -> checkableStepResult.getScore() < 5);
    }
    private Boolean isRejectedScore(Integer sumatory) {
        return hasAnyCheckableStepLessThanFive() || sumatory.compareTo(40) <= 0;
    }

    private Boolean isApprovedScore(Integer sumatory) {
        return sumatory.compareTo(80) >= 0;
    }
}
