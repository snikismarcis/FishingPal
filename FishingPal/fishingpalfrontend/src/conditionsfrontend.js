import { useEffect, useState } from "react";

export default function ConditionsDashboard() {
  const [metrics, setMetrics] = useState([]);
  const [location, setLocation] = useState("");
  const [summary, setSummary] = useState("");

  async function fetchConditions() {
    try {
      const res = await fetch("http://localhost:8080/conditions");
      const data = await res.json();
      setMetrics(data.metrics);
      setLocation(data.location.name);
      setSummary(data.summary);
    } catch (err) {
      console.error("Failed to fetch conditions", err);
    }
  }

  useEffect(() => {
    fetchConditions();
    const interval = setInterval(fetchConditions, 60 * 1000);
    return () => clearInterval(interval);
  }, []);

  const favorabilityColors = {
    FAVORABLE: "green",
    NEUTRAL: "orange",
    UNFAVORABLE: "red",
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h1>Fishing Conditions for {location}</h1>
      <p>{summary}</p>
      <div>
        {metrics.map((m) => (
          <div
            key={m.metric}
            style={{
              border: "1px solid #ccc",
              borderRadius: "5px",
              margin: "10px 0",
              padding: "10px",
              backgroundColor: "#f9f9f9",
            }}
          >
            <h3 style={{ color: favorabilityColors[m.favorability] }}>
              {m.metric.toUpperCase()}: {m.value} {m.unit}
            </h3>
            <p>{m.reasoning}</p>
          </div>
        ))}
      </div>
    </div>
  );
}