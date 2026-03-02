package com.fishingpal.FishingPal.service.evaluator;

import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import org.springframework.stereotype.Component;

@Component
public class PressureEvaluator implements MetricEvaluator {

    private static final double RAPID_CHANGE_THRESHOLD = 2.0; // hPa per hour

    @Override
    public MetricAssessment evaluate(WeatherSnapshot weather) {
        double current = weather.getPressure();
        double previous = weather.getPreviousPressure();
        double change = current - previous;

        Favorability favorability;
        String reasoning;

        if (change < -RAPID_CHANGE_THRESHOLD) {
            favorability = Favorability.FAVORABLE;
            reasoning = String.format(
                    "Pressure dropping rapidly (%.1f hPa/hr). Steadily falling pressure usually means lower light level -> hunting adventage for predatory fish before they settle for worse weather conditions.",
                    change);
        } else if (change > RAPID_CHANGE_THRESHOLD) {
            favorability = Favorability.UNFAVORABLE;
            reasoning = String.format(
                    "Pressure rising rapidly (+%.1f hPa/hr). Rapid change conditions often cause fish lethargy, fish are less likely to be feeding.",
                    change);
        } else {
            favorability = Favorability.NEUTRAL;
            reasoning = String.format(
                    "Pressure is stable (%.1f hPa/hr change). Steady conditions mean normal fish activity. Some might be hungry, others - not so much.",
                    change);
        }

        return new MetricAssessment("pressure", favorability, current, "hPa", reasoning);
    }
}
