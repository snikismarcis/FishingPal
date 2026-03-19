import { useEffect, useState, useCallback } from "react"
import { ConditionsResponse } from "../Types/metric"

const API_BASE = "http://localhost:8080"

export function useConditions(species: string, lat?: number, lon?: number) {
  const [data, setData] = useState<ConditionsResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchConditions = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const params = new URLSearchParams({ species })
      if (lat !== undefined) params.set("lat", lat.toFixed(4))
      if (lon !== undefined) params.set("lon", lon.toFixed(4))
      const res = await fetch(`${API_BASE}/conditions?${params}`)
      if (!res.ok) throw new Error(`HTTP ${res.status}`)
      const json: ConditionsResponse = await res.json()
      setData(json)
    } catch (err: any) {
      setError(err.message || "Failed to fetch conditions")
    } finally {
      setLoading(false)
    }
  }, [species, lat, lon])

  useEffect(() => {
    fetchConditions()
    const interval = setInterval(fetchConditions, 5 * 60 * 1000)
    return () => clearInterval(interval)
  }, [fetchConditions])

  return { data, loading, error, refetch: fetchConditions }
}

export function useSpeciesList() {
  const [species, setSpecies] = useState<string[]>([])

  useEffect(() => {
    fetch(`${API_BASE}/species`)
      .then((res) => res.json())
      .then(setSpecies)
      .catch(() => setSpecies(["perch", "pike", "bream"]))
  }, [])

  return species
}
