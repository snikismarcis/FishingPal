package com.fishingpal.FishingPal.infrastructure.weather.openmeteo;

import java.util.List;

public class OpenMeteoResponse {

    public Current current;
    public Hourly hourly;
    public Daily daily;

    public static class Current {
        public String time;
        public double temperature_2m;
        public double apparent_temperature;
        public double relative_humidity_2m;
        public int is_day;
        public double precipitation;
        public double cloud_cover;
        public double surface_pressure;
        public double wind_speed_10m;
        public double wind_direction_10m;
    }

    public static class Hourly {
        public List<String> time;
        public List<Double> temperature_2m;
        public List<Double> precipitation;
        public List<Double> surface_pressure;
        public List<Double> wind_speed_10m;
        public List<Double> wind_direction_10m;
        public List<Double> relative_humidity_2m;
        public List<Double> cloud_cover;
    }

    public static class Daily {
        public List<String> time;
        public List<Double> temperature_2m_max;
        public List<Double> temperature_2m_min;
        public List<Double> uv_index_max;
        public List<Double> precipitation_sum;
        public List<Double> wind_speed_10m_max;
    }
}
