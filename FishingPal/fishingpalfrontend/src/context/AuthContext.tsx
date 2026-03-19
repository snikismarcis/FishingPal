import { createContext, useContext, useState, useCallback } from "react"
import type { ReactNode } from "react"

const API_BASE = "http://localhost:8080"

type AuthState = {
  token: string | null
  username: string | null
}

type AuthContextType = AuthState & {
  login: (username: string, password: string) => Promise<void>
  register: (username: string, email: string, password: string) => Promise<void>
  logout: () => void
  isLoggedIn: boolean
}

const AuthContext = createContext<AuthContextType | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [auth, setAuth] = useState<AuthState>(() => {
    const token = localStorage.getItem("fp_token")
    const username = localStorage.getItem("fp_username")
    return { token, username }
  })

  const persist = useCallback((token: string, username: string) => {
    localStorage.setItem("fp_token", token)
    localStorage.setItem("fp_username", username)
    setAuth({ token, username })
  }, [])

  const login = useCallback(async (username: string, password: string) => {
    const res = await fetch(`${API_BASE}/api/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    })
    if (!res.ok) {
      const msg = res.status === 401 ? "Invalid credentials" : "Login failed"
      throw new Error(msg)
    }
    const data = await res.json()
    persist(data.token, data.username)
  }, [persist])

  const register = useCallback(async (username: string, email: string, password: string) => {
    const res = await fetch(`${API_BASE}/api/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, email, password }),
    })
    if (!res.ok) {
      const body = await res.json().catch(() => null)
      throw new Error(body?.message || "Registration failed")
    }
    const data = await res.json()
    persist(data.token, data.username)
  }, [persist])

  const logout = useCallback(() => {
    localStorage.removeItem("fp_token")
    localStorage.removeItem("fp_username")
    setAuth({ token: null, username: null })
  }, [])

  return (
    <AuthContext.Provider value={{ ...auth, login, register, logout, isLoggedIn: !!auth.token }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error("useAuth must be used within AuthProvider")
  return ctx
}
