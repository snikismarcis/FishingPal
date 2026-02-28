package com.fishingpal.FishingPal.service.evaluator;

import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import org.springframework.stereotype.Component;

@Component
public class TemperatureEvaluator implements MetricEvaluator {

    @Override
    public MetricAssessment evaluate(WeatherSnapshot weather) {
        double temp = weather.getTemperature();

        Favorability favorability;
        String reasoning;

        if (temp < 4) {
            favorability = Favorability.UNFAVORABLE;
            reasoning = String.format(
                    "Temperature is very low (%.1f°C). Cold water slows fish metabolism significantly.", temp);
        } else if (temp < 8) {
            favorability = Favorability.NEUTRAL;
            reasoning = String.format(
                    "Temperature is cool (%.1f°C). Fish are less active but still feeding occasionally.", temp);
        } else if (temp <= 24) {
            favorability = Favorability.FAVORABLE;
            reasoning = String.format(
                    "Temperature is in the optimal range (%.1f°C). Fish metabolism and feeding activity are high.", temp);
        } else if (temp <= 28) {
            favorability = Favorability.NEUTRAL;
            reasoning = String.format(
                    "Temperature is warm (%.1f°C). Fish may seek deeper, cooler water.", temp);
        } else {
            favorability = Favorability.UNFAVORABLE;
            reasoning = String.format(
                    "Temperature is very high (%.1f°C). Heat stress reduces dissolved oxygen and fish activity.", temp);
        }

        return new MetricAssessment("temperature", favorability, temp, "°C", reasoning);
    }
}
