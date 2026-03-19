package com.fishingpal.FishingPal.service.evaluator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fishingpal.FishingPal.domain.Favorability;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.infrastructure.weather.rules.RuleLoader;

public abstract class RangeRuleEvaluator {

    private final RuleLoader ruleLoader;

    protected RangeRuleEvaluator(RuleLoader ruleLoader) {
        this.ruleLoader = ruleLoader;
    }

    protected MetricAssessment evaluateRange(String metric, double value, String species) {

        JsonNode ruleSet = ruleLoader.getRule(metric);

        String unit = ruleSet.path("unit").asText();

        JsonNode rules;
        if (ruleSet.has("species") && ruleSet.path("species").has(species)) {
            rules = ruleSet.path("species").path(species);
        } else if (ruleSet.has("species")) {
            rules = ruleSet.path("species").elements().next();
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
                        reason
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
