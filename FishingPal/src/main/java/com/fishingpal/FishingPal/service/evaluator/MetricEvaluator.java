package com.fishingpal.FishingPal.service.evaluator;

import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;

public interface MetricEvaluator {
    MetricAssessment evaluate(WeatherSnapshot weather);
}
