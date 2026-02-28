package com.fishingpal.FishingPal.service.evaluator;

import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import org.springframework.stereotype.Component;

@Component
public class WindEvaluator implements MetricEvaluator {

    @Override
    public MetricAssessment evaluate(WeatherSnapshot weather) {
        double wind = weather.getWindSpeed();

        Favorability favorability;
        String reasoning;

        if (wind < 5) {
            favorability = Favorability.NEUTRAL;
            reasoning = String.format(
                    "Wind is calm (%.1f km/h). Low wind means less water mixing and oxygenation.", wind);
        } else if (wind <= 20) {
            favorability = Favorability.FAVORABLE;
            reasoning = String.format(
                    "Light to moderate wind (%.1f km/h). Good oxygenation and prey distribution along wind-blown shores.",
                    wind);
        } else {
            favorability = Favorability.UNFAVORABLE;
            reasoning = String.format(
                    "Strong wind (%.1f km/h). Dangerous conditions and excessive turbidity reduce feeding.", wind);
        }

        return new MetricAssessment("wind", favorability, wind, "km/h", reasoning);
    }
}
