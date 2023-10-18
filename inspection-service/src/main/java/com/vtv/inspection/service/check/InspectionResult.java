package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.InspectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.vtv.inspection.model.domain.InspectionStatus.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionResult {
    private List<CheckableStepResult> results;

    private Boolean hasAnyCheckableStepLessThanFive() {
        return results.stream().anyMatch(checkableStepResult -> checkableStepResult.getScore() < 5);
    }

    public Integer calculateScore() {
        return results.stream()
                .mapToInt(CheckableStepResult::getScore)
                .sum();
    }

    private Boolean isRejectedScore(Integer sumatory) {
        return hasAnyCheckableStepLessThanFive() || sumatory.compareTo(40) <= 0;
    }

    private Boolean isApprovedScore(Integer sumatory) {
        return sumatory.compareTo(80) >= 0;
    }

    public InspectionStatus getStatus() {
        if (results.isEmpty()) {
            return PENDING;
        }

        final Integer sumatory = calculateScore();

        return isRejectedScore(sumatory) ? REJECTED :
                isApprovedScore(sumatory) ? APPROVED : APPROVED_WITH_COMMENTS;

    }
}
