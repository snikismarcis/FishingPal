package com.fishingpal.FishingPal.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataDto {

    private double currentTemp;
    private double currentHumidity;
    private double currentCloudCover;
    private double currentWindSpeed;
    private double currentWindDirection;
    private double currentPressure;
    private double currentPrecipitation;
    private int isDay;

    private List<String> hourlyTime;
    private List<Double> hourlyTemp;
    private List<Double> hourlyHumidity;
    private List<Double> hourlyCloudCover;
    private List<Double> hourlyWind;
    private List<Double> hourlyWindDirection;
    private List<Double> hourlyPrecipitation;
    private List<Double> hourlyPressure;

    private double tempMax;
    private double tempMin;
    private double uvIndexMax;
    private double precipitationSum;
    private double windSpeedMax;
    private String sunrise;
    private String sunset;
}
