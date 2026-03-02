package com.fishingpal.FishingPal.service.evaluator;

import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import org.springframework.stereotype.Component;

@Component
public class PrecipitationEvaluator implements MetricEvaluator {

    @Override
    public MetricAssessment evaluate(WeatherSnapshot weather) {
        double precip = weather.getPrecipitation();

        Favorability favorability;
        String reasoning;

        if (precip < 0.1) {
            favorability = Favorability.NEUTRAL;
            reasoning = "No significant precipitation. Stable conditions with no rain-triggered feeding activity. Let's see how the fish are feeling today. :)";
        } else if (precip <= 4) {
            favorability = Favorability.FAVORABLE;
            reasoning = String.format(
                    "Light to moderate rain (%.1f mm/h). Rain increases oxygen levels in water, makes insects fly lower and washes minerals from shore into water, triggering feeding near shore.",
                    precip);
        } else {
            favorability = Favorability.UNFAVORABLE;
            reasoning = String.format(
                    "Heavy rain (%.1f mm/h). Excessive turbidity and strong water disturbance from rain can make feeding difficult for fish, as well as scare the fish into hiding.",
                    precip);
        }

        return new MetricAssessment("precipitation", favorability, precip, "mm/h", reasoning);
    }
}
