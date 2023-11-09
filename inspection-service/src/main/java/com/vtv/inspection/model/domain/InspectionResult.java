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
public class InspectionResult { //TODO: Ver si es necesario que exista
    //todo: Deberia tener un finished date, el score aca dentro y los calcular quizas deberian ser parte del service
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
                isApprovedScore(sumatory) ? APPROVED : APPROVED_WITH_COMMENTS;

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
