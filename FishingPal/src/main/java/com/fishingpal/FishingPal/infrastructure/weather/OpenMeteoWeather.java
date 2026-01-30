package com.fishingpal.FishingPal.infrastructure.weather;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import com.fishingpal.FishingPal.infrastructure.weather.openmeteo.OpenMeteoResponse;

@Service
public class OpenMeteoWeather implements WeatherProvider {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public WeatherSnapshot getCurrentWeather(double lat, double lon) {

        String url = String.format(
            "https://api.open-meteo.com/v1/forecast" +
            "?latitude=%f&longitude=%f" +
            "&hourly=surface_pressure,precipitation" +
            "&current_weather=true",
            lat, lon
        );

        OpenMeteoResponse response =
            restTemplate.getForObject(url, OpenMeteoResponse.class);

        double pressure = response.hourly.surface_pressure.get(0);
        double precipitation = response.hourly.precipitation.get(0);

        return new WeatherSnapshot(
                response.current_weather.temperature,
                pressure,
                response.current_weather.windspeed,
                precipitation,
                Instant.now()
        );
    }
}
