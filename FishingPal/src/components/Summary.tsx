type Props = {
  summary: string
}

export default function Summary({ summary }: Props) {
  return (
    <div className="bg-blue-50 border border-blue-200 rounded-xl p-4">

      <h2 className="font-semibold mb-1">
        Conditions Summary
      </h2>

      <p className="text-sm">
        {summary}
      </p>

    </div>
  )
}