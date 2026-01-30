package com.fishingpal.FishingPal.infrastructure.weather;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;

public interface WeatherProvider {
    WeatherSnapshot getCurrentWeather(double lat, double lon);
}
