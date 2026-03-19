type Props = {
  summary: string
  species: string
  sunrise?: string
  sunset?: string
  uvIndex?: number
}

export default function Summary({ summary, species, sunrise, sunset, uvIndex }: Props) {
  const isHighlyFavorable = summary.includes("Highly favorable")
  const isChallenging = summary.includes("Challenging")

  const accentColor = isHighlyFavorable
    ? "border-l-green-500"
    : isChallenging
    ? "border-l-red-500"
    : "border-l-amber-500"

  const textColor = isHighlyFavorable
    ? "text-green-700"
    : isChallenging
    ? "text-red-700"
    : "text-amber-700"

  return (
    <div className={`bg-white rounded-2xl border border-gray-200 ${accentColor} p-6 shadow-sm`}>
      <div className="flex items-center justify-between mb-3">
        <h2 className="font-bold text-lg text-gray-900">Conditions Summary</h2>
        <span className="text-xs text-gray-400 capitalize">
          Targeting: <span className="text-blue-600 font-medium">{species}</span>
        </span>
      </div>

      <p className={`text-sm font-medium ${textColor}`}>
        {summary}
      </p>

      {(sunrise || sunset || uvIndex !== undefined) && (
        <div className="flex gap-6 mt-4 pt-3 border-t border-gray-100">
          {sunrise && (
            <div className="text-xs">
              <span className="text-gray-400">Sunrise</span>
              <p className="text-gray-900 font-medium">{sunrise.split('T')[1]?.slice(0, 5)}</p>
            </div>
          )}
          {sunset && (
            <div className="text-xs">
              <span className="text-gray-400">Sunset</span>
              <p className="text-gray-900 font-medium">{sunset.split('T')[1]?.slice(0, 5)}</p>
            </div>
          )}
          {uvIndex !== undefined && (
            <div className="text-xs">
              <span className="text-gray-400">UV Index</span>
              <p className="text-gray-900 font-medium">{uvIndex.toFixed(1)}</p>
            </div>
          )}
        </div>
      )}
    </div>
  )
}
