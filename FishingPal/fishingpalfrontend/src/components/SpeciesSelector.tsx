type Props = {
  species: string[]
  selected: string
  onChange: (species: string) => void
}

export default function SpeciesSelector({ species, selected, onChange }: Props) {
  return (
    <div className="flex items-center gap-2">
      <span className="text-sm text-gray-400">Target Species</span>
      <div className="flex gap-1 bg-gray-100 rounded-xl p-1">
        {species.map((s) => (
          <button
            key={s}
            onClick={() => onChange(s)}
            className={`px-4 py-2 rounded-lg text-sm font-medium capitalize transition-all duration-200
              ${
                s === selected
                  ? "bg-blue-600 text-white shadow-sm"
                  : "text-gray-500 hover:text-gray-900 hover:bg-gray-200"
              }`}
          >
            {s}
          </button>
        ))}
      </div>
    </div>
  )
}
