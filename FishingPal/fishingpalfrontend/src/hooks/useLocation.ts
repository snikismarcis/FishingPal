import { useEffect, useState, useCallback } from "react"

type Location = {
  lat: number
  lon: number
  name: string
}

const GEOCODE_API = "https://geocoding-api.open-meteo.com/v1/search"

const DEFAULT_LOCATION: Location = { lat: 56.9, lon: 24.1, name: "Riga" }

export function useLocation() {
  const [location, setLocation] = useState<Location>(DEFAULT_LOCATION)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const reverseGeocode = useCallback(async (_lat: number, _lon: number): Promise<string> => {
    return "Current Location"
  }, [])

  useEffect(() => {
    if (!navigator.geolocation) {
      setError("Geolocation not supported")
      setLoading(false)
      return
    }

    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        const { latitude, longitude } = pos.coords
        const name = await reverseGeocode(latitude, longitude)
        setLocation({ lat: latitude, lon: longitude, name })
        setLoading(false)
      },
      () => {
        setError("Location access denied — using default")
        setLoading(false)
      },
      { timeout: 8000 }
    )
  }, [reverseGeocode])

  const setManualLocation = useCallback((lat: number, lon: number, name: string) => {
    setLocation({ lat, lon, name })
    setError(null)
  }, [])

  return { location, loading, error, setManualLocation }
}

export async function searchCities(query: string): Promise<Array<{
  name: string
  country: string
  admin1: string
  lat: number
  lon: number
}>> {
  if (query.length < 2) return []
  const res = await fetch(`${GEOCODE_API}?name=${encodeURIComponent(query)}&count=5&language=en`)
  if (!res.ok) return []
  const data = await res.json()
  if (!data.results) return []
  return data.results.map((r: any) => ({
    name: r.name,
    country: r.country || "",
    admin1: r.admin1 || "",
    lat: r.latitude,
    lon: r.longitude,
  }))
}
