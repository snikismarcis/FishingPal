import MetricCard from "./MetricCard.tsx"
import { Metric } from "../Types/metric"

type Props = {
  metrics: Metric[]
}

export default function MetricsGrid({ metrics }: Props) {
  return (
    <div className="grid grid-cols-2 gap-4">

      {metrics.map((m) => (
        <MetricCard key={m.metric} metric={m} />
      ))}

    </div>
  )
}