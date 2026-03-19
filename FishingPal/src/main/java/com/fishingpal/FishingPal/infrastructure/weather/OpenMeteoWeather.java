package com.fishingpal.FishingPal.infrastructure.weather;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fishingpal.FishingPal.api.dto.WeatherDataDto;
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

    @Override
    public WeatherDataDto getFullWeatherData(double lat, double lon) {
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

        WeatherDataDto dto = new WeatherDataDto();
        
        dto.setCurrentTemp(response.current.temperature_2m);
        dto.setCurrentHumidity(response.current.relative_humidity_2m);
        dto.setCurrentCloudCover(response.current.cloud_cover);
        dto.setCurrentWindSpeed(response.current.wind_speed_10m);
        dto.setCurrentWindDirection(response.current.wind_direction_10m);
        dto.setCurrentPressure(response.current.surface_pressure);
        dto.setCurrentPrecipitation(response.current.precipitation);
        dto.setIsDay(response.current.is_day);
        
        dto.setHourlyTime(response.hourly.time);
        dto.setHourlyTemp(response.hourly.temperature_2m);
        dto.setHourlyHumidity(response.hourly.relative_humidity_2m);
        dto.setHourlyCloudCover(response.hourly.cloud_cover);
        dto.setHourlyWind(response.hourly.wind_speed_10m);
        dto.setHourlyWindDirection(response.hourly.wind_direction_10m);
        dto.setHourlyPrecipitation(response.hourly.precipitation);
        dto.setHourlyPressure(response.hourly.surface_pressure);
        
        dto.setTempMax(response.daily.temperature_2m_max.get(0));
        dto.setTempMin(response.daily.temperature_2m_min.get(0));
        dto.setUvIndexMax(response.daily.uv_index_max.get(0));
        dto.setPrecipitationSum(response.daily.precipitation_sum.get(0));
        dto.setWindSpeedMax(response.daily.wind_speed_10m_max.get(0));
        dto.setSunrise(response.daily.sunrise.get(0));
        dto.setSunset(response.daily.sunset.get(0));
        
        log.info("Full weather data fetched for lat={}, lon={}", lat, lon);
        return dto;
    }

    private int findCurrentHourIndex(List<String> hourlyTimes, String currentTime) {
        String currentHour = currentTime.substring(0, 13);
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
