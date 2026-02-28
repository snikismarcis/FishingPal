package com.fishingpal.FishingPal.domain;

public record MetricAssessment(
        String metricName,
        Favorability favorability,
        double value,
        String unit,
        String reasoning
) {}
