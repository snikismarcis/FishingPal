import { useEffect, useState } from "react"
import MetricsGrid from "../components/MetricsGrid.tsx"
import SummaryCard from "../components/Summary.tsx"
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

    <div className="min-h-screen bg-gray-100">

      {/* NAVBAR */}

      <nav className="absolute top-0 w-full z-20 backdrop-blur-md bg-white/10 border-b border-white/20">

        <div className="max-w-6xl mx-auto flex items-center justify-between px-6 py-4 text-white">

          <div className="absolute inset-0 flex items-center justify-center">
            <div className="text-xl font-semibold text-white">
              FishingPal
            </div>
          </div>

          <div className="flex gap-8 text-sm">

            <button className="hover:opacity-80">Conditions</button>
            <button className="hover:opacity-80">Calendar</button>
            <button className="hover:opacity-80">Log</button>
            <button className="hover:opacity-80">Community</button>
            <button className="hover:opacity-80">Knowledge</button>

          </div>

        </div>

      </nav>


      {/* HERO */}

      <section className="relative h-[70vh] w-full overflow-hidden">

        <video
          autoPlay
          muted
          loop
          playsInline
          className="absolute w-full h-full object-cover"
        >
          <source src="/hero-video.mp4" type="video/mp4" />
        </video>

        <div className="absolute inset-0 bg-black/40"></div>
        
        <div className="relative z-10 flex flex-col h-full justify-between">

          <div className="max-w-5xl mx-auto px-6 text-white">

            <h1 className="text-5xl font-semibold mb-4">
              FishingPal
            </h1>

            <p className="text-xl opacity-90 mb-2">
              Today's Fishing Outlook
            </p>

            <p className="text-lg opacity-80 max-w-xl">
              {summary}
            </p>

          </div>

        </div>

      </section>


      {/* EXISTING DASHBOARD */}

      <div className="p-6">

        <div className="max-w-3xl mx-auto space-y-6">

          <SummaryCard summary={summary} />

          <MetricsGrid metrics={metrics} />

        </div>

      </div>

    </div>

  )
}