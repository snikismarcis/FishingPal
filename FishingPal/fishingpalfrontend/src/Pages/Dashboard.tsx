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

    const fadeDistance = 300;
    const fadeProgress = Math.min(scrollY / fadeDistance, 1);

  return (
    <div className="min-h-screen w-full overflow-x-hidden">

      <section className="relative h-screen w-full">
        <video
          autoPlay
          muted
          loop
          playsInline
          className="absolute top-0 left-0 w-full h-full object-cover"
        >
          <source src="/hero-video.mp4" type="video/mp4" />
        </video>

        <div className="absolute inset-0 bg-black/30"></div>

        <div className="absolute left-0 top-0 h-full z-10 flex items-center justify-center"
        style={{ opacity: 1 - fadeProgress }}
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
          className={`fixed top-4 left-4 text-2xl font-bold z-30 transition-all duration-500 
            ${fadeProgress === 1 ? "opacity-100 translate-y-0" : "opacity-0 -translate-y-2"}
            ${scrollY < 600 ? "text-white" : "text-blue-600"}
            `}
        >
          FishingPal
        </div>

        <nav
          className={`fixed top-4 right-6 z-40 flex items-center gap-3 px-3 py-2 rounded-xl
          backdrop-blur-md transition-all duration-500
          ${
            fadeProgress === 1
              ? "bg-white shadow-md text-black-600"
              : "bg-transparent text-white"
          }`}
        >
          {["Conditions", "Calendar", "Log", "Community", "Knowledge"].map((item) => (
            <button
              key={item}
              className={`px-3 py-1 text-sm rounded-lg transition-all duration-200
              ${
                fadeProgress === 1
                  ? "hover:bg-blue-600 hover:text-white"
                  : "hover:bg-white/20 hover:text-white"
              }`}
            >
              {item}
            </button>
          ))}
        </nav>

        {/* TODO change to current weather conditions */}
        <div className="absolute bottom-12 left-1/2 transform -translate-x-1/2 text-center z-10 max-w-3xl px-6">
          <p className="text-xl text-white/90 mb-2">Today's Fishing Outlook</p>
          <p className="text-lg text-white/80">{summary}</p>
        </div>
      </section>

      {/* TODO change to divided species cards */}
      <div className="p-6 bg-gray-100">
        <div className="max-w-3xl mx-auto space-y-6">
          <SummaryCard summary={summary} />
          <MetricsGrid metrics={metrics} />
        </div>
      </div>
    </div>
  );
}