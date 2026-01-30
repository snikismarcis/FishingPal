package com.fishingpal.FishingPal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeciesRecommendationDto {
    private String speciesName;
    private double score;
    private List<ScoreFactorDto> scoreFactors;
}