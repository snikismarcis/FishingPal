package com.fishingpal.FishingPal.service;

import com.fishingpal.FishingPal.api.dto.ConditionsResponseDto;
import com.fishingpal.FishingPal.api.dto.LocationDto;
import com.fishingpal.FishingPal.api.dto.MetricAssessmentDto;
import com.fishingpal.FishingPal.domain.MetricAssessment;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import com.fishingpal.FishingPal.infrastructure.weather.WeatherProvider;
import com.fishingpal.FishingPal.service.evaluator.MetricEvaluator;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConditionsService {

    private static final Logger log = LoggerFactory.getLogger(ConditionsService.class);

    private final WeatherProvider weatherProvider;
    private final List<MetricEvaluator> evaluators;
    private final ConditionsAggregator aggregator;

    public ConditionsService(WeatherProvider weatherProvider,
                             List<MetricEvaluator> evaluators,
                             ConditionsAggregator aggregator) {
        this.weatherProvider = weatherProvider;
        this.evaluators = evaluators;
        this.aggregator = aggregator;
    }

    public ConditionsResponseDto getCurrentConditions(double lat, double lon) {
        log.info("Fetching current weather for lat={}, lon={}", lat, lon);
        WeatherSnapshot weather = weatherProvider.getCurrentWeather(lat, lon);

        List<MetricAssessment> assessments = evaluators.stream()
                .map(e -> e.evaluate(weather))
                .toList();

        String summary = aggregator.summarize(assessments);

        List<MetricAssessmentDto> metricDtos = assessments.stream()
                .map(a -> new MetricAssessmentDto(
                        a.metricName(),
                        a.value(),
                        a.unit(),
                        a.favorability().name(),
                        a.reasoning()))
                .toList();

        return new ConditionsResponseDto(
                new LocationDto(String.format("%.4f,%.4f", lat, lon),
                        String.format("%.2f°N, %.2f°E", lat, lon)),
                Instant.now(),
                summary,
                metricDtos);
    }
}
