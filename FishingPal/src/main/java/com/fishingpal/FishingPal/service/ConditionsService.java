package com.fishingpal.FishingPal.service;

import com.fishingpal.FishingPal.api.dto.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class ConditionsService {

    public ConditionsResponseDto getCurrentConditions() {
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
