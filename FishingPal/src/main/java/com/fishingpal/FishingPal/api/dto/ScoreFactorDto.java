package com.fishingpal.FishingPal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreFactorDto {
    private String factorName;
    private double factorValue;
}