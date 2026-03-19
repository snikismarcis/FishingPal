import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import MetricsGrid from "../components/MetricsGrid.tsx";
import SummaryCard from "../components/Summary.tsx";
import SpeciesSelector from "../components/SpeciesSelector.tsx";
import LocationSelector from "../components/LocationSelector.tsx";
import { useConditions, useSpeciesList } from "../hooks/useConditions";
import { useLocation } from "../hooks/useLocation";
import { useAuth } from "../context/AuthContext.tsx";

const NAV_ITEMS = [
  { label: "Conditions", to: "/" },
  { label: "Log", to: "/log" },
  { label: "Calendar", to: "/" },
  { label: "Community", to: "/" },
  { label: "Knowledge", to: "/" },
];

export default function Dashboard() {
  const [selectedSpecies, setSelectedSpecies] = useState("perch");
  const [scrollY, setScrollY] = useState(0);
  const speciesList = useSpeciesList();
  const { isLoggedIn, username, logout } = useAuth();
  const { location, loading: locationLoading, setManualLocation } = useLocation();
  const { data, loading, error, refetch } = useConditions(
    selectedSpecies, location.lat, location.lon
  );

  useEffect(() => {
    const handleScroll = () => setScrollY(window.scrollY);
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const fadeDistance = 300;
  const fadeProgress = Math.min(scrollY / fadeDistance, 1);

  return (
    <div className="min-h-screen w-full overflow-x-hidden">

      <section className="relative h-screen w-full bg-ocean-900">
        <video
          autoPlay
          muted
          loop
          playsInline
          className="absolute top-0 left-0 w-full h-full object-cover"
        >
          <source src="/hero-video.mp4" type="video/mp4" />
        </video>

        

        <div className="absolute left-0 top-0 h-full z-10 flex items-end justify-center"
          style={{ opacity: 0.7 - fadeProgress }}
        >
          <h1
            className="text-[7vw] font-bold tracking-wider leading-[0.8em] select-none"
            style={{
              writingMode: "vertical-rl",
              color: "rgba(255,255,255,0.8)",
              transform: "rotate(180deg)",
            }}
          >
            F I S H I N G
          </h1>
          <h1
            className="text-[7vw] font-bold tracking-wider leading-[0.8em] select-none"
            style={{
              writingMode: "vertical-rl",
              color: "rgba(255,255,255,0.8)",
              transform: "rotate(180deg)",
            }}
          >
            P A L
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
              ? "bg-white/95 shadow-md text-gray-800"
              : "bg-transparent text-white"
          }`}
        >
          {NAV_ITEMS.map((item) => (
            <Link
              key={item.label}
              to={item.to}
              className={`px-3 py-1 text-sm rounded-lg transition-all duration-200
              ${
                fadeProgress === 1
                  ? "hover:bg-blue-600 hover:text-white"
                  : "hover:bg-white/20 hover:text-white"
              }`}
            >
              {item.label}
            </Link>
          ))}
          <span className="w-px h-5 bg-current opacity-20"></span>
          {isLoggedIn ? (
            <div className="flex items-center gap-2">
              <span className="text-sm font-medium">{username}</span>
              <button
                onClick={logout}
                className={`px-3 py-1 text-xs rounded-lg transition ${
                  fadeProgress === 1
                    ? "hover:bg-red-50 hover:text-red-600"
                    : "hover:bg-white/20"
                }`}
              >
                Logout
              </button>
            </div>
          ) : (
            <Link
              to="/login"
              className={`px-3 py-1 text-sm font-medium rounded-lg transition-all duration-200 ${
                fadeProgress === 1
                  ? "bg-blue-600 text-white hover:bg-blue-700"
                  : "bg-white/20 hover:bg-white/30"
              }`}
            >
              Sign In
            </Link>
          )}
        </nav>

        <div className="absolute bottom-12 left-1/2 transform -translate-x-1/2 text-center z-10 max-w-3xl px-6">
          <p className="text-xl text-white/90 mb-2">Today's Fishing Outlook</p>
          <p className="text-lg text-white/80">{data?.summary || "Loading conditions..."}</p>
        </div>
      </section>

      <div className="px-6 py-8 bg-gray-50 min-h-screen">
        <div className="max-w-7xl mx-auto space-y-6">

          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
            <div className="flex items-center gap-4">
              <h2 className="text-2xl font-bold text-gray-900">
                Current Conditions
              </h2>
              <LocationSelector
                locationName={location.name}
                onSelect={setManualLocation}
              />
            </div>
            {speciesList.length > 0 && (
              <SpeciesSelector
                species={speciesList}
                selected={selectedSpecies}
                onChange={setSelectedSpecies}
              />
            )}
          </div>

          {(loading || locationLoading) && (
            <div className="flex items-center justify-center py-20">
              <div className="animate-spin rounded-full h-8 w-8 border-2 border-blue-600 border-t-transparent"></div>
              <span className="ml-3 text-gray-400">
                {locationLoading ? "Getting your location..." : "Fetching conditions..."}
              </span>
            </div>
          )}

          {error && !loading && (
            <div className="bg-red-50 border border-red-200 rounded-2xl p-6 text-center">
              <p className="text-red-700 font-medium mb-2">Failed to load conditions</p>
              <p className="text-sm text-gray-500 mb-4">{error}</p>
              <button
                onClick={refetch}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg text-sm hover:bg-blue-700 transition"
              >
                Retry
              </button>
            </div>
          )}

          {data && !loading && !locationLoading && (
            <>
              <SummaryCard
                summary={data.summary}
                species={data.species}
                sunrise={data.weatherData?.sunrise}
                sunset={data.weatherData?.sunset}
                uvIndex={data.weatherData?.uvIndexMax}
              />
              <MetricsGrid
                metrics={data.metrics}
                weatherData={data.weatherData}
              />
            </>
          )}
        </div>
      </div>
    </div>
  );
}
