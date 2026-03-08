import { Metric } from "../Types/metric"

type Props = {
  metric: Metric
}

export default function MetricCard({ metric }: Props) {

  const color =
    metric.favorability === "FAVORABLE"
      ? "text-green-600"
      : metric.favorability === "UNFAVORABLE"
      ? "text-red-600"
      : "text-gray-500"

  return (
    <div className="bg-white rounded-xl shadow p-4">

      <h3 className="font-semibold capitalize">
        {metric.metric}
      </h3>

      <p className="text-2xl mt-2">
        {metric.value} {metric.unit}
      </p>

      <p className={`${color} text-sm mt-1`}>
        {metric.favorability}
      </p>

      <p className="text-xs text-gray-500 mt-2">
        {metric.reasoning}
      </p>

    </div>
  )
}