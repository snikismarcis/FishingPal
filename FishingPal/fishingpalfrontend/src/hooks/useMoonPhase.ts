const SYNODIC_MONTH = 29.53059

const KNOWN_NEW_MOON = new Date("2000-01-06T18:14:00Z").getTime()

type MoonPhase = {
  phaseName: string
  emoji: string
  illumination: number
  daysToFullMoon: number
  age: number
}

function getMoonAge(date: Date): number {
  const diff = date.getTime() - KNOWN_NEW_MOON
  const days = diff / (1000 * 60 * 60 * 24)
  return ((days % SYNODIC_MONTH) + SYNODIC_MONTH) % SYNODIC_MONTH
}

function getPhaseInfo(age: number): { name: string; emoji: string } {
  if (age < 1.85) return { name: "New Moon", emoji: "🌑" }
  if (age < 7.38) return { name: "Waxing Crescent", emoji: "🌒" }
  if (age < 9.23) return { name: "First Quarter", emoji: "🌓" }
  if (age < 13.77) return { name: "Waxing Gibbous", emoji: "🌔" }
  if (age < 15.77) return { name: "Full Moon", emoji: "🌕" }
  if (age < 21.15) return { name: "Waning Gibbous", emoji: "🌖" }
  if (age < 23.00) return { name: "Last Quarter", emoji: "🌗" }
  if (age < 27.68) return { name: "Waning Crescent", emoji: "🌘" }
  return { name: "New Moon", emoji: "🌑" }
}

function getIllumination(age: number): number {
  const phase = age / SYNODIC_MONTH
  return Math.round((1 - Math.cos(phase * 2 * Math.PI)) / 2 * 100)
}

function getDaysToFullMoon(age: number): number {
  const fullMoonAge = SYNODIC_MONTH / 2 // ~14.77
  if (age <= fullMoonAge) {
    return Math.round(fullMoonAge - age)
  }
  return Math.round(SYNODIC_MONTH - age + fullMoonAge)
}

export function useMoonPhase(): MoonPhase {
  const now = new Date()
  const age = getMoonAge(now)
  const { name, emoji } = getPhaseInfo(age)

  return {
    phaseName: name,
    emoji,
    illumination: getIllumination(age),
    daysToFullMoon: getDaysToFullMoon(age),
    age: Math.round(age),
  }
}
