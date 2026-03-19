package com.fishingpal.FishingPal.service.evaluator;

import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import com.fishingpal.FishingPal.infrastructure.weather.rules.RuleLoader;
import org.springframework.stereotype.Component;

@Component
public class TemperatureEvaluator extends RangeRuleEvaluator implements MetricEvaluator {

    public TemperatureEvaluator(RuleLoader ruleLoader) {
        super(ruleLoader);
    }

    @Override
    public MetricAssessment evaluate(WeatherSnapshot weather, String species) {
        return evaluateRange("temperature", weather.getTemperature(), species);
    }
}