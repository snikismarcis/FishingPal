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

    public double getTemperature() {
        return temperature;
    }
    
    public double getPressure() {
        return pressure;
    }
    public double getWindSpeed() {
        return windSpeed;
    }
    public double getPrecipitation() {
        return precipitation;
    }
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Weather snapshot{ " +
        "temperature" + temperature +
        "pressure" + pressure +
        "windSpeed" + windSpeed +
        "precipitation" + precipitation +
        "timestamp" + timestamp +
        "}";
    }
}
