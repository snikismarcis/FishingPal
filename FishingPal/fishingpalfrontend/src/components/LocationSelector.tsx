import { useState, useRef, useEffect, useCallback } from "react"
import { searchCities } from "../hooks/useLocation"

type City = {
  name: string
  country: string
  admin1: string
  lat: number
  lon: number
}

type Props = {
  locationName: string
  onSelect: (lat: number, lon: number, name: string) => void
}

export default function LocationSelector({ locationName, onSelect }: Props) {
  const [open, setOpen] = useState(false)
  const [query, setQuery] = useState("")
  const [results, setResults] = useState<City[]>([])
  const [searching, setSearching] = useState(false)
  const inputRef = useRef<HTMLInputElement>(null)
  const containerRef = useRef<HTMLDivElement>(null)
  const debounceRef = useRef<ReturnType<typeof setTimeout>>(undefined)

  useEffect(() => {
    function handleClick(e: MouseEvent) {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false)
      }
    }
    document.addEventListener("mousedown", handleClick)
    return () => document.removeEventListener("mousedown", handleClick)
  }, [])

  useEffect(() => {
    if (open && inputRef.current) inputRef.current.focus()
  }, [open])

  const handleSearch = useCallback((value: string) => {
    setQuery(value)
    if (debounceRef.current) clearTimeout(debounceRef.current)
    if (value.length < 2) {
      setResults([])
      return
    }
    setSearching(true)
    debounceRef.current = setTimeout(async () => {
      const cities = await searchCities(value)
      setResults(cities)
      setSearching(false)
    }, 300)
  }, [])

  const handleSelect = (city: City) => {
    const label = city.admin1
      ? `${city.name}, ${city.admin1}, ${city.country}`
      : `${city.name}, ${city.country}`
    onSelect(city.lat, city.lon, label)
    setOpen(false)
    setQuery("")
    setResults([])
  }

  return (
    <div ref={containerRef} className="relative">
      <button
        onClick={() => setOpen(!open)}
        className="flex items-center gap-1.5 text-sm text-gray-600 hover:text-gray-900 transition-colors"
      >
        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
            d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
            d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
        </svg>
        <span className="max-w-[200px] truncate">{locationName}</span>
        <svg className="w-3 h-3 opacity-50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
        </svg>
      </button>

      {open && (
        <div className="absolute top-full mt-2 right-0 w-72 bg-white rounded-xl shadow-lg border border-gray-200 z-50 overflow-hidden">
          <div className="p-2">
            <input
              ref={inputRef}
              type="text"
              value={query}
              onChange={(e) => handleSearch(e.target.value)}
              placeholder="Search city..."
              className="w-full px-3 py-2 text-sm bg-gray-50 border border-gray-200 rounded-lg
                focus:outline-none focus:border-blue-400 focus:ring-1 focus:ring-blue-400
                text-gray-900 placeholder-gray-400"
            />
          </div>

          {searching && (
            <div className="px-4 py-3 text-xs text-gray-400">Searching...</div>
          )}

          {results.length > 0 && (
            <ul className="max-h-48 overflow-y-auto">
              {results.map((city, i) => (
                <li key={`${city.lat}-${city.lon}-${i}`}>
                  <button
                    onClick={() => handleSelect(city)}
                    className="w-full text-left px-4 py-2.5 text-sm hover:bg-gray-50 transition-colors flex items-center gap-2"
                  >
                    <svg className="w-3.5 h-3.5 text-gray-400 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                        d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                    </svg>
                    <div>
                      <span className="text-gray-900 font-medium">{city.name}</span>
                      <span className="text-gray-400 ml-1 text-xs">
                        {city.admin1 ? `${city.admin1}, ` : ""}{city.country}
                      </span>
                    </div>
                  </button>
                </li>
              ))}
            </ul>
          )}

          {query.length >= 2 && !searching && results.length === 0 && (
            <div className="px-4 py-3 text-xs text-gray-400">No results found</div>
          )}
        </div>
      )}
    </div>
  )
}
