package com.fishingpal.FishingPal.service;

import com.fishingpal.FishingPal.api.dto.*;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import com.fishingpal.FishingPal.infrastructure.weather.WeatherProvider;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ConditionsService {

    private static final Logger log = LoggerFactory.getLogger(ConditionsService.class);

    private final WeatherProvider weatherProvider;

    public ConditionsService(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public ConditionsResponseDto getCurrentConditions() {

        log.info("Fetching current weather for lat=56.9, lon=24.1");
        WeatherSnapshot weather = weatherProvider.getCurrentWeather(56.9, 24.1);
        log.debug("Weather snapshot received: {}", weather);

        return new ConditionsResponseDto(
                new LocationDto("aiviekste-city", "Aiviekste City"),
                Instant.now(),
                72,
                "Good conditions for bream and perch",
                Map.of(
                        "airPressure", new MetricDto(
                                1012.0,
                                "hPa",
                                "FALLING",
                                "Falling pressure often increased feeding activity."
                        ),
                        "airTemperature", new MetricDto(
                                14.0,
                                "°C",
                                "STABLE",
                                "Moderate temperature supports steady metabolism."
                        )
                ),
                List.of(
                new SpeciesRecommendationDto(
                    "BREAM",
                    78,
                    List.of(
                        new ScoreFactorDto("PRESSURE_TREND", 10),
                        new ScoreFactorDto("TEMPERATURE", 8)
                    )
                ),
                new SpeciesRecommendationDto(
                    "PERCH",
                    65,
                    List.of(
                        new ScoreFactorDto("WIND_STABILITY", 7),
                        new ScoreFactorDto("TEMPERATURE", 6)
                    )
                )
            ),
            List.of(
                new ScoreFactorDto("PRESSURE_TREND", 18),
                new ScoreFactorDto("TEMPERATURE", 12)
            )
        );
    }
}
