package com.fishingpal.FishingPal.domain;

import java.time.Instant;
import java.util.List;

public record ConditionsAssessment(
        String locationName,
        double latitude,
        double longitude,
        Instant timestamp,
        String summary,
        List<MetricAssessment> metrics
) {}
