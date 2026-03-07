package com.fishingpal.FishingPal.service.evaluator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.infrastructure.weather.rules.RuleLoader;
import org.springframework.beans.factory.annotation.Value;

public abstract class RangeRuleEvaluator {

    private final RuleLoader ruleLoader;

    @Value("${fishingpal.target-species:perch}")
    private String targetSpecies;

    protected RangeRuleEvaluator(RuleLoader ruleLoader) {
        this.ruleLoader = ruleLoader;
    }

    protected MetricAssessment evaluateRange(String metric, double value) {

        JsonNode ruleSet = ruleLoader.getRule(metric);

        String unit = ruleSet.path("unit").asText();

        JsonNode rules;
        if (ruleSet.has("species")) {
            rules = ruleSet.path("species").path(targetSpecies);
        } else {
            rules = ruleSet.path("rules");
        }

        for (JsonNode rule : rules) {

            double min = rule.path("min").asDouble();
            double max = rule.path("max").asDouble();

            if (value >= min && value < max) {

                Favorability favorability =
                        Favorability.valueOf(rule.path("favorability").asText());

                String reason = rule.path("reason").asText();

                return new MetricAssessment(
                        metric,
                        favorability,
                        value,
                        unit,
                        String.format("%s (%.1f %s)", reason, value, unit)
                );
            }
        }

        return new MetricAssessment(
                metric,
                Favorability.NEUTRAL,
                value,
                unit,
                "No rule matched."
        );
    }
}