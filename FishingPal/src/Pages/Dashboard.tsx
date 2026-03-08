import { useEffect, useState } from "react"
import MetricsGrid from "../components/MetricsGrid"
import SummaryCard from "../components/Summary"
import { Metric } from "../Types/metric"

export default function Dashboard() {

  const [metrics, setMetrics] = useState<Metric[]>([])
  const [summary, setSummary] = useState("")

  useEffect(() => {

    fetch("http://localhost:8080/conditions")
      .then(res => res.json())
      .then(data => {
        setMetrics(data.metrics)
        setSummary(data.summary)
      })

  }, [])

  return (
    <div className="min-h-screen bg-gray-100 p-6">

      <div className="max-w-3xl mx-auto space-y-6">

        <SummaryCard summary={summary} />

        <MetricsGrid metrics={metrics} />

      </div>

    </div>
  )
}