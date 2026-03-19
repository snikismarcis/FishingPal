export type Favorability = "FAVORABLE" | "NEUTRAL" | "UNFAVORABLE"

export type Metric = {
  metric: string
  value: number
  unit: string
  favorability: Favorability
  reasoning: string
}

export type WeatherData = {
  currentTemp: number
  currentHumidity: number
  currentCloudCover: number
  currentWindSpeed: number
  currentWindDirection: number
  currentPressure: number
  currentPrecipitation: number
  isDay: number
  hourlyTime: string[]
  hourlyTemp: number[]
  hourlyHumidity: number[]
  hourlyCloudCover: number[]
  hourlyWind: number[]
  hourlyWindDirection: number[]
  hourlyPrecipitation: number[]
  hourlyPressure: number[]
  tempMax: number
  tempMin: number
  uvIndexMax: number
  precipitationSum: number
  windSpeedMax: number
  sunrise: string
  sunset: string
}

export type ConditionsResponse = {
  location: { id: string; name: string }
  timestamp: string
  species: string
  summary: string
  metrics: Metric[]
  weatherData: WeatherData
}
