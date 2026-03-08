import { useEffect, useState } from "react";
import MetricsGrid from "../components/MetricsGrid.tsx";
import SummaryCard from "../components/Summary.tsx";
import { Metric } from "../Types/metric";

export default function Dashboard() {
  const [metrics, setMetrics] = useState<Metric[]>([]);
  const [summary, setSummary] = useState("");
  const [scrollY, setScrollY] = useState(0);

  useEffect(() => {
    fetch("http://localhost:8080/conditions")
      .then((res) => res.json())
      .then((data) => {
        setMetrics(data.metrics);
        setSummary(data.summary);
      });
      const handleScroll = () => setScrollY(window.scrollY);
      window.addEventListener("scroll", handleScroll);
      return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <div className="min-h-screen w-full overflow-x-hidden">

      {/* HERO / FULL SCREEN VIDEO */}
      <section className="relative h-screen w-full">
        {/* Video */}
        <video
          autoPlay
          muted
          loop
          playsInline
          className="absolute top-0 left-0 w-full h-full object-cover"
        >
          <source src="/hero-video.mp4" type="video/mp4" />
        </video>

        {/* Overlay */}
        <div className="absolute inset-0 bg-black/30"></div>


        <div className="absolute left-0 top-0 h-full z-10 flex items-center justify-center"
        style={{ opacity: 1 - Math.min(scrollY / 100, 1) }}
        >
          <h1
            className="text-[7vw] font-bold tracking-wider leading-[0.8em] select-none"
            style={{
              writingMode: "vertical-rl",
              color: "rgba(255,255,255,0.8)",
              transform: "rotate(180deg)",
            }}
          >
            F I S H I N G P A L
          </h1>
        </div>

        <div
          className={`absolute top-4 left-4 text-2xl font-bold z-30 transition-colors duration-500 ${
            scrollY < 100 ? "text-white" : "text-blue-600"
          }`}
        >
          FishingPal
        </div>

        {/* Navbar on top-right */}
        <nav
          className={`fixed top-0 right-0 z-40 flex gap-6 p-4 transition-colors duration-500 ${
            scrollY < 300 ? "bg-transparent text-white" : "bg-white text-blue-600"
          }`}
        >
          {["Conditions", "Calendar", "Log", "Community", "Knowledge"].map(
            (item) => (
              <button
                key={item}
                className="px-3 py-1 hover:bg-white/20 rounded transition"
              >
                {item}
              </button>
            )
          )}
        </nav>

        {/* Today's Fishing Conditions at bottom */}
        <div className="absolute bottom-12 left-1/2 transform -translate-x-1/2 text-center z-10 max-w-3xl px-6">
          <p className="text-xl text-white/90 mb-2">Today's Fishing Outlook</p>
          <p className="text-lg text-white/80">{summary}</p>
        </div>
      </section>

      {/* DASHBOARD BELOW HERO */}
      <div className="p-6 bg-gray-100">
        <div className="max-w-3xl mx-auto space-y-6">
          <SummaryCard summary={summary} />
          <MetricsGrid metrics={metrics} />
        </div>
      </div>
    </div>
  );
}