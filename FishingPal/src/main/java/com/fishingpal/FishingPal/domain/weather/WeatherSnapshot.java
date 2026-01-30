package com.fishingpal.FishingPal.domain.weather;

import java.time.Instant;

public class WeatherSnapshot {

    private final double temperature;
    private final double pressure;
    private final double windSpeed;
    private final double precipitation;
    private final Instant timestamp;

    public WeatherSnapshot(
            double temperature,
            double pressure,
            double windSpeed,
            double precipitation,
            Instant timestamp
    ) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return String.format(
            "Temp=%.1f°C, Pressure=%.1fhPa, Wind=%.1fm/s, Precip=%.1fmm",
            temperature, pressure, windSpeed, precipitation
        );
    }
}
