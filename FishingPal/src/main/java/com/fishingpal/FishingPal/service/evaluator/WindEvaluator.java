package com.fishingpal.FishingPal.service.evaluator;

import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import com.fishingpal.FishingPal.infrastructure.weather.rules.RuleLoader;
import org.springframework.stereotype.Component;

@Component
public class WindEvaluator extends RangeRuleEvaluator implements MetricEvaluator {

    public WindEvaluator(RuleLoader ruleLoader) {
        super(ruleLoader);
    }

    @Override
    public MetricAssessment evaluate(WeatherSnapshot weather) {
        return evaluateRange("wind", weather.getWindSpeed());
    }
}
