package com.fishingpal.FishingPal.infrastructure.weather;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;
import com.fishingpal.FishingPal.infrastructure.weather.openmeteo.OpenMeteoResponse;

@Service
public class OpenMeteoWeather implements WeatherProvider {

    private static final Logger log = LoggerFactory.getLogger(OpenMeteoWeather.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public WeatherSnapshot getCurrentWeather(double lat, double lon) {

        String url = String.format(
                "https://api.open-meteo.com/v1/forecast" +
                        "?latitude=%f&longitude=%f" +
                        "&hourly=surface_pressure,precipitation" +
                        "&current_weather=true",
                lat, lon);

        OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);

        // Find the current hour index from the hourly time array
        int currentIndex = findCurrentHourIndex(response.hourly.time, response.current_weather.time);

        double pressure = response.hourly.surface_pressure.get(currentIndex);
        double previousPressure = currentIndex > 0
                ? response.hourly.surface_pressure.get(currentIndex - 1)
                : pressure;
        double precipitation = response.hourly.precipitation.get(currentIndex);

        log.info("Weather fetched: temp={}°C, pressure={} hPa, prevPressure={} hPa, wind={} km/h, precip={} mm/h",
                response.current_weather.temperature, pressure, previousPressure,
                response.current_weather.windspeed, precipitation);

        return new WeatherSnapshot(
                response.current_weather.temperature,
                pressure,
                previousPressure,
                response.current_weather.windspeed,
                precipitation,
                Instant.now());
    }

    private int findCurrentHourIndex(List<String> hourlyTimes, String currentTime) {
        // current_weather.time is like "2026-02-28T15:00" — match the hour
        String currentHour = currentTime.substring(0, 13); // "2026-02-28T15"
        for (int i = 0; i < hourlyTimes.size(); i++) {
            if (hourlyTimes.get(i).startsWith(currentHour)) {
                return i;
            }
        }
        log.warn("Could not match current hour '{}', defaulting to index 0", currentHour);
        return 0;
    }
}
