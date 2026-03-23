package com.fishingpal.FishingPal.service;

import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConditionsAggregator {

    public Favorability aggregate(List<MetricAssessment> metrics) {

        boolean anyUnfavorable = metrics.stream()
                .anyMatch(m -> m.favorability() == Favorability.UNFAVORABLE);

        long favorableCount = metrics.stream()
                .filter(m -> m.favorability() == Favorability.FAVORABLE)
                .count();

        if (!anyUnfavorable && favorableCount >= 1) {
            return Favorability.FAVORABLE;
        }

        if (anyUnfavorable) {
            return Favorability.UNFAVORABLE;
        }

        return Favorability.NEUTRAL;
    }

    public String summarize(List<MetricAssessment> metrics) {
        Favorability overall = aggregate(metrics);

        switch (overall) {
            case FAVORABLE:
                return "Highly favorable conditions — all environmental factors support good fishing activity.";
            case UNFAVORABLE:
                return "Challenging conditions — some environmental factors are unfavorable for fishing.";
            default:
                return "Conditions are mixed — some factors are favorable while others are neutral.";
        }
    }
}