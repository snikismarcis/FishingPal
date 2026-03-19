import { useMoonPhase } from "../hooks/useMoonPhase"

export default function MoonPhaseCard() {
  const { phaseName, emoji, illumination, daysToFullMoon, age } = useMoonPhase()

  return (
    <div className="bg-white rounded-2xl border border-gray-200 shadow-sm p-5 flex flex-col">
      <h3 className="font-semibold text-gray-900 text-sm mb-4">Moon</h3>

      <div className="flex items-start gap-4 flex-1">
        <div className="text-5xl leading-none">{emoji}</div>

        <div className="flex-1 space-y-2">
          <div>
            <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
              {phaseName}
            </p>
            <p className="text-[11px] text-gray-400">Today</p>
          </div>

          <div className="space-y-1.5 pt-1">
            <div className="flex justify-between text-xs">
              <span className="text-gray-500">Illumination</span>
              <span className="font-medium text-gray-900">{illumination}%</span>
            </div>
            <div className="flex justify-between text-xs">
              <span className="text-gray-500">Moon Age</span>
              <span className="font-medium text-gray-900">{age} days</span>
            </div>
            <div className="flex justify-between text-xs">
              <span className="text-gray-500">Next Full Moon</span>
              <span className="font-medium text-gray-900">
                {daysToFullMoon === 0 ? "Today" : `${daysToFullMoon} days`}
              </span>
            </div>
            <div>
              <p className="text-xs text-gray-500 leading-relaxed italic">
                Bright moonlight increases nocturnal feeding, usually during full or new moon.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
