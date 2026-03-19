package com.fishingpal.FishingPal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionsResponseDto {
    private LocationDto location;
    private Instant timestamp;
    private String species;
    private String summary;
    private List<MetricAssessmentDto> metrics;
    private WeatherDataDto weatherData;
}
