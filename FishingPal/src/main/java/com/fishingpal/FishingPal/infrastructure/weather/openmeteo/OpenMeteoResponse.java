package com.fishingpal.FishingPal.infrastructure.weather.openmeteo;

import java.util.List;

public class OpenMeteoResponse {

    public CurrentWeather current_weather;
    public Hourly hourly;

    public static class CurrentWeather {
        public double temperature;
        public double windspeed;
        public String time;
    }

    public static class Hourly {
        public List<Double> surface_pressure;
        public List<Double> precipitation;
        public List<String> time;
    }
}
