package com.fishingpal.FishingPal.infrastructure.weather;

import com.fishingpal.FishingPal.api.dto.WeatherDataDto;
import com.fishingpal.FishingPal.domain.weather.WeatherSnapshot;

public interface WeatherProvider {
    WeatherSnapshot getCurrentWeather(double lat, double lon);
    WeatherDataDto getFullWeatherData(double lat, double lon);
}
