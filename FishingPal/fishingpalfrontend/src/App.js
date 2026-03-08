import React, { useEffect, useState } from "react";

function App() {
  const [conditions, setConditions] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const lat = 56.9;
  const lon = 24.1;

  useEffect(() => {
    const fetchConditions = async () => {
      try {
        const res = await fetch(`/conditions?lat=${lat}&lon=${lon}`);
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
        const data = await res.json();
        setConditions(data);
      } catch (err) {
        console.error("Failed to fetch conditions:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchConditions();
  }, [lat, lon]);

  if (loading) return <div>Loading fishing conditions...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!conditions) return <div>No data available.</div>;

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h1>Fishing Conditions</h1>
      <p>
        <strong>Location:</strong> {conditions.location.name}
      </p>
      <p>
        <strong>Summary:</strong> {conditions.summary}
      </p>
      <h2>Metrics:</h2>
      <ul>
        {conditions.metrics.map((metric) => (
          <li key={metric.metric} style={{ marginBottom: "10px" }}>
            <strong>{metric.metric.toUpperCase()}:</strong> {metric.value} {metric.unit} —{" "}
            <span style={{ color: metric.favorability === "FAVORABLE" ? "green" : metric.favorability === "UNFAVORABLE" ? "red" : "gray" }}>
              {metric.favorability}
            </span>
            <br />
            <em>{metric.reasoning}</em>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
