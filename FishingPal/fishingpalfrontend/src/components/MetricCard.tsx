import { Metric, Favorability } from "../Types/metric"
import {
  AreaChart, Area, ResponsiveContainer, YAxis, Tooltip, XAxis
} from "recharts"

type Props = {
  metric: Metric
  hourlyData?: { time: string; value: number }[]
}

const BADGE_STYLES: Record<Favorability, string> = {
  FAVORABLE: "bg-green-50 text-green-700 border-green-200",
  NEUTRAL: "bg-amber-50 text-amber-700 border-amber-200",
  UNFAVORABLE: "bg-red-50 text-red-700 border-red-200",
}

const CHART_COLORS: Record<Favorability, string> = {
  FAVORABLE: "#22C55E",
  NEUTRAL: "#F59E0B",
  UNFAVORABLE: "#EF4444",
}

export default function MetricCard({ metric, hourlyData }: Props) {
  const chartColor = CHART_COLORS[metric.favorability]
  const badgeStyle = BADGE_STYLES[metric.favorability]

  return (
    <div className="bg-white rounded-2xl border border-gray-200 shadow-sm p-5 hover:shadow-md transition-all duration-300">
      <div className="flex items-start justify-between mb-3">
        <div className="flex items-center gap-2">
          <h3 className="font-semibold capitalize text-gray-900 text-sm">
            {metric.metric}
          </h3>
        </div>
        <span className={`text-[11px] font-medium px-2 py-0.5 rounded-full border ${badgeStyle}`}>
          {metric.favorability}
        </span>
      </div>

      <p className="text-3xl font-bold text-gray-900 mb-3">
        {metric.value.toFixed(1)}
        <span className="text-sm font-normal text-gray-400 ml-1">{metric.unit}</span>
      </p>

      {hourlyData && hourlyData.length > 0 && (
        <div className="h-24 -mx-2 mb-3">
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart data={hourlyData}>
              <defs>
                <linearGradient id={`grad-${metric.metric}`} x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor={chartColor} stopOpacity={0.15} />
                  <stop offset="100%" stopColor={chartColor} stopOpacity={0} />
                </linearGradient>
              </defs>
              <XAxis
                dataKey="time"
                axisLine={false}
                tickLine={false}
                tick={{ fontSize: 9, fill: '#9CA3AF' }}
                interval={Math.floor(hourlyData.length / 5)}
              />
              <YAxis hide domain={['auto', 'auto']} />
              <Tooltip
                contentStyle={{
                  backgroundColor: '#FFFFFF',
                  border: '1px solid #E5E7EB',
                  borderRadius: '8px',
                  fontSize: '12px',
                  color: '#111827'
                }}
                labelStyle={{ color: '#6B7280' }}
              />
              <Area
                type="monotone"
                dataKey="value"
                stroke={chartColor}
                strokeWidth={2}
                fill={`url(#grad-${metric.metric})`}
                dot={false}
                activeDot={{ r: 3, fill: chartColor }}
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>
      )}

      <p className="text-xs text-gray-500 leading-relaxed italic">
        {metric.reasoning}
      </p>
    </div>
  )
}
