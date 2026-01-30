package com.fishingpal.FishingPal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricDto {
    private double value;
    private String unit;
    private String trend;
    private String tip;
}