package com.fishingpal.FishingPal.infrastructure.weather;

import java.time.Instant;
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
            "&daily=temperature_2m_max,temperature_2m_min,apparent_temperature_min,apparent_temperature_max," +
            "sunrise,sunset,daylight_duration,sunshine_duration,uv_index_max,precipitation_sum," +
            "wind_speed_10m_max,wind_direction_10m_dominant" +
            "&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation_probability," +
            "precipitation,surface_pressure,cloud_cover,wind_speed_10m,wind_direction_10m" +
            "&current=temperature_2m,apparent_temperature,relative_humidity_2m,is_day,precipitation," +
            "cloud_cover,surface_pressure,wind_speed_10m,wind_direction_10m" +
            "&timezone=auto" +
            "&wind_speed_unit=ms",
            lat, lon
        );

        OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);

        int currentIndex = findCurrentHourIndex(response.hourly.time, response.current.time);

        double pressure = response.hourly.surface_pressure.get(currentIndex);
        double previousPressure = currentIndex > 0
            ? response.hourly.surface_pressure.get(currentIndex - 1)
            : pressure;
        double precipitation = response.hourly.precipitation.get(currentIndex);

        log.info("Weather fetched: temp={}°C, pressure={} hPa, prevPressure={} hPa, wind={} m/s, precip={} mm/h",
            response.current.temperature_2m, pressure, previousPressure,
            response.current.wind_speed_10m, precipitation
        );

        return new WeatherSnapshot(
            response.current.temperature_2m,
            pressure,
            previousPressure,
            response.current.wind_speed_10m,
            precipitation,
            Instant.now()
        );
    }

    private int findCurrentHourIndex(List<String> hourlyTimes, String currentTime) {
        // current.weather.time is like "2026-02-28T15:00" — match the hour
        String currentHour = currentTime.substring(0, 13); // "2026-02-28T15"
        log.info("Current Hour=" + currentHour + " Current Time=" + currentTime);
        for (int i = 0; i < hourlyTimes.size(); i++) {
            if (hourlyTimes.get(i).startsWith(currentHour)) {
                return i;
            }
        }
        log.warn("Could not match current hour '{}', defaulting to index 0", currentHour);
        return 0;
    }
}
