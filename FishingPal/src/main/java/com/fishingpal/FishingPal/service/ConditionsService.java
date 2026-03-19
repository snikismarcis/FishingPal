package com.fishingpal.FishingPal.service;

import com.fishingpal.FishingPal.api.dto.ConditionsResponseDto;
import com.fishingpal.FishingPal.api.dto.LocationDto;
import com.fishingpal.FishingPal.api.dto.MetricAssessmentDto;
import com.fishingpal.FishingPal.api.dto.WeatherDataDto;
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

    public ConditionsResponseDto getCurrentConditions(double lat, double lon, String species) {
        log.info("Fetching conditions for lat={}, lon={}, species={}", lat, lon, species);
        WeatherSnapshot weather = weatherProvider.getCurrentWeather(lat, lon);
        WeatherDataDto weatherData = weatherProvider.getFullWeatherData(lat, lon);

        List<MetricAssessment> assessments = evaluators.stream()
                .map(e -> e.evaluate(weather, species))
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
                        String.format("%.2f\u00b0N, %.2f\u00b0E", lat, lon)),
                Instant.now(),
                species,
                summary,
                metricDtos,
                weatherData);
    }
}
