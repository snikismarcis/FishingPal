package com.fishingpal.FishingPal.service.evaluator;

import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import com.fishingpal.FishingPal.infrastructure.weather.rules.RuleLoader;
import org.springframework.stereotype.Component;

@Component
public class PrecipitationEvaluator extends RangeRuleEvaluator implements MetricEvaluator {

    public PrecipitationEvaluator(RuleLoader ruleLoader) {
        super(ruleLoader);
    }

    @Override
    public MetricAssessment evaluate(WeatherSnapshot weather) {
        return evaluateRange("precipitation", weather.getPrecipitation());
    }
}