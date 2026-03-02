package com.fishingpal.FishingPal.service.evaluator;

import java.util.List;

import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;

public class OverallEvaluation {
    public Favorability evaluate(List<MetricAssessment> metrics) {

        long favorable = metrics.stream().filter(m -> m.favorability() == Favorability.FAVORABLE).count();
        long unfavorable = metrics.stream().filter(m -> m.favorability() == Favorability.UNFAVORABLE).count();

        if (unfavorable >= 2) return Favorability.UNFAVORABLE;
        if (favorable > unfavorable) return Favorability.FAVORABLE;
        return Favorability.NEUTRAL;
    }
}
