import { BrowserRouter, Routes, Route } from "react-router-dom"
import { AuthProvider } from "./context/AuthContext.tsx"
import Dashboard from "./Pages/Dashboard.tsx"
import LoginPage from "./Pages/LoginPage.tsx"
import RegisterPage from "./Pages/RegisterPage.tsx"
import LogPage from "./Pages/LogPage.tsx"
import './index.css';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/log" element={<LogPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
