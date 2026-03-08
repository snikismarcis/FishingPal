export type Metric = {
  metric: string
  value: number
  unit: string
  favorability: "FAVORABLE" | "NEUTRAL" | "UNFAVORABLE"
  reasoning: string
}