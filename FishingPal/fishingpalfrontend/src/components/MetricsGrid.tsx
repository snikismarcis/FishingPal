import MetricCard from "./MetricCard.tsx"
import MoonPhaseCard from "./MoonPhaseCard.tsx"
import { Metric, WeatherData } from "../Types/metric"

type Props = {
  metrics: Metric[]
  weatherData?: WeatherData
}

const HOURLY_KEYS: Record<string, keyof WeatherData> = {
  temperature: "hourlyTemp",
  pressure: "hourlyPressure",
  wind: "hourlyWind",
  precipitation: "hourlyPrecipitation",
}

function buildHourlyData(weatherData: WeatherData, key: keyof WeatherData) {
  const times = weatherData.hourlyTime
  const values = weatherData[key] as number[]
  if (!times || !values) return []

  const now = new Date()
  const currentHour = now.getHours()
  const startIdx = Math.max(0, currentHour)
  const slice = Math.min(24, times.length - startIdx)

  return times.slice(startIdx, startIdx + slice).map((t, i) => ({
    time: new Date(t).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    value: values[startIdx + i],
  }))
}

export default function MetricsGrid({ metrics, weatherData }: Props) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      {metrics.map((m) => {
        const hourlyKey = HOURLY_KEYS[m.metric]
        const hourlyData = weatherData && hourlyKey
          ? buildHourlyData(weatherData, hourlyKey)
          : undefined

        return (
          <MetricCard
            key={m.metric}
            metric={m}
            hourlyData={hourlyData}
          />
        )
      })}

      {weatherData && (
        <MetricCard
          metric={{
            metric: "cloudCover",
            value: weatherData.currentCloudCover,
            unit: "%",
            favorability: weatherData.currentCloudCover < 50 ? "FAVORABLE" : weatherData.currentCloudCover < 80 ? "NEUTRAL" : "UNFAVORABLE",
            reasoning: weatherData.currentCloudCover < 50
              ? "Clear skies allow sunlight to warm water and stimulate feeding."
              : weatherData.currentCloudCover < 80
              ? "Partial cloud cover provides comfortable fishing conditions."
              : "Overcast skies may reduce fish surface activity."
          }}
          hourlyData={buildHourlyData(weatherData, "hourlyCloudCover")}
        />
      )}

      <MoonPhaseCard />
    </div>
  )
}
