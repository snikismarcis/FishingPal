package com.fishingpal.FishingPal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionsResponseDto {
    private LocationDto location;
    private Instant timestamp;
    private int score;
    private String summary;
    private Map<String, MetricDto> metrics;
    private List<SpeciesRecommendationDto> speciesRecommendations;
    private List<ScoreFactorDto> scoreFactors;
}