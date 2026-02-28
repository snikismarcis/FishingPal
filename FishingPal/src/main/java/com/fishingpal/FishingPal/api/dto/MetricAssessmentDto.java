package com.fishingpal.FishingPal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricAssessmentDto {
    private String metric;
    private double value;
    private String unit;
    private String favorability;
    private String reasoning;
}
