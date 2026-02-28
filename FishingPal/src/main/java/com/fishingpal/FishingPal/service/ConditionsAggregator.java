package com.fishingpal.FishingPal.service;

import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConditionsAggregator {

    public String summarize(List<MetricAssessment> assessments) {
        long favorableCount = assessments.stream()
                .filter(a -> a.favorability() == Favorability.FAVORABLE)
                .count();
        boolean anyUnfavorable = assessments.stream()
                .anyMatch(a -> a.favorability() == Favorability.UNFAVORABLE);

        if (anyUnfavorable) {
            return "Challenging conditions — some environmental factors are unfavorable for fishing.";
        }
        if (favorableCount == assessments.size()) {
            return "Highly favorable conditions — all environmental factors support good fishing activity.";
        }
        return "Conditions are mixed — some factors are favorable while others are neutral.";
    }
}
