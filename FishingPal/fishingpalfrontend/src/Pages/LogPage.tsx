import { useState, useEffect, useCallback } from "react"
import { useAuth } from "../context/AuthContext.tsx"
import { Link } from "react-router-dom"

const API_BASE = "http://localhost:8080"

type Post = {
  id: string
  content: string
  username: string
  createdAt: string
  likeCount: number
  likedByMe: boolean
}

export default function LogPage() {
  const { token, username, isLoggedIn } = useAuth()
  const [posts, setPosts] = useState<Post[]>([])
  const [content, setContent] = useState("")
  const [loading, setLoading] = useState(true)
  const [posting, setPosting] = useState(false)

  const headers = useCallback((): Record<string, string> => {
    const h: Record<string, string> = { "Content-Type": "application/json" }
    if (token) h["Authorization"] = `Bearer ${token}`
    return h
  }, [token])

  const fetchPosts = useCallback(async () => {
    try {
      const res = await fetch(`${API_BASE}/api/logs`, {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      })
      if (res.ok) setPosts(await res.json())
    } catch {}
    finally { setLoading(false) }
  }, [token])

  useEffect(() => { fetchPosts() }, [fetchPosts])

  const handlePost = async () => {
    if (!content.trim()) return
    setPosting(true)
    try {
      const res = await fetch(`${API_BASE}/api/logs`, {
        method: "POST",
        headers: headers(),
        body: JSON.stringify({ content: content.trim() }),
      })
      if (res.ok) {
        setContent("")
        fetchPosts()
      }
    } finally { setPosting(false) }
  }

  const handleLike = async (id: string) => {
    await fetch(`${API_BASE}/api/logs/${id}/like`, {
      method: "POST",
      headers: headers(),
    })
    fetchPosts()
  }

  const handleDelete = async (id: string) => {
    await fetch(`${API_BASE}/api/logs/${id}`, {
      method: "DELETE",
      headers: headers(),
    })
    fetchPosts()
  }

  const timeAgo = (iso: string) => {
    const diff = Date.now() - new Date(iso).getTime()
    const mins = Math.floor(diff / 60000)
    if (mins < 1) return "just now"
    if (mins < 60) return `${mins}m`
    const hrs = Math.floor(mins / 60)
    if (hrs < 24) return `${hrs}h`
    return `${Math.floor(hrs / 24)}d`
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="sticky top-0 bg-white/95 backdrop-blur-sm border-b border-gray-200 z-20">
        <div className="max-w-2xl mx-auto px-4 py-3 flex items-center justify-between">
          <Link to="/" className="text-xl font-bold text-blue-600">FishingPal</Link>
          <h1 className="text-lg font-semibold text-gray-900">Fishing Log</h1>
          <div className="w-20 text-right">
            {isLoggedIn ? (
              <span className="text-sm text-gray-500">{username}</span>
            ) : (
              <Link to="/login" className="text-sm text-blue-600 font-medium">Sign In</Link>
            )}
          </div>
        </div>
      </div>

      <div className="max-w-2xl mx-auto px-4 py-6 space-y-4">
        {isLoggedIn && (
          <div className="bg-white rounded-2xl border border-gray-200 shadow-sm p-4">
            <div className="flex items-start gap-3">
              <div className="w-9 h-9 rounded-full bg-blue-100 text-blue-600 flex items-center justify-center text-sm font-bold flex-shrink-0">
                {username?.[0]?.toUpperCase()}
              </div>
              <div className="flex-1">
                <textarea
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  placeholder="What's happening on the water?"
                  maxLength={500}
                  rows={3}
                  className="w-full resize-none border-0 bg-transparent text-gray-900 text-sm
                    placeholder-gray-400 focus:outline-none"
                />
                <div className="flex items-center justify-between pt-2 border-t border-gray-100">
                  <span className="text-xs text-gray-400">{content.length}/500</span>
                  <button
                    onClick={handlePost}
                    disabled={!content.trim() || posting}
                    className="px-4 py-1.5 bg-blue-600 text-white text-sm font-medium rounded-full
                      hover:bg-blue-700 transition disabled:opacity-40"
                  >
                    {posting ? "Posting..." : "Post"}
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}

        {!isLoggedIn && (
          <div className="bg-blue-50 border border-blue-200 rounded-2xl p-4 text-center">
            <p className="text-sm text-blue-800">
              <Link to="/login" className="font-medium underline">Sign in</Link> to post your fishing updates
            </p>
          </div>
        )}

        {loading && (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-6 w-6 border-2 border-blue-600 border-t-transparent"></div>
          </div>
        )}

        {!loading && posts.length === 0 && (
          <p className="text-center text-gray-400 py-12 text-sm">No posts yet. Be the first!</p>
        )}

        {posts.map((post) => (
          <div key={post.id} className="bg-white rounded-2xl border border-gray-200 shadow-sm p-4">
            <div className="flex items-start gap-3">
              <div className="w-9 h-9 rounded-full bg-gray-100 text-gray-600 flex items-center justify-center text-sm font-bold flex-shrink-0">
                {post.username[0].toUpperCase()}
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2">
                  <span className="font-semibold text-sm text-gray-900">{post.username}</span>
                  <span className="text-xs text-gray-400">{timeAgo(post.createdAt)}</span>
                </div>
                <p className="text-sm text-gray-700 mt-1 whitespace-pre-wrap">{post.content}</p>

                <div className="flex items-center gap-4 mt-3">
                  {isLoggedIn && (
                    <button
                      onClick={() => handleLike(post.id)}
                      className={`flex items-center gap-1 text-xs transition
                        ${post.likedByMe ? "text-red-500" : "text-gray-400 hover:text-red-500"}`}
                    >
                      {post.likedByMe ? "♥" : "♡"} {post.likeCount > 0 && post.likeCount}
                    </button>
                  )}
                  {!isLoggedIn && post.likeCount > 0 && (
                    <span className="text-xs text-gray-400">♥ {post.likeCount}</span>
                  )}
                  {isLoggedIn && post.username === username && (
                    <button
                      onClick={() => handleDelete(post.id)}
                      className="text-xs text-gray-400 hover:text-red-500 transition"
                    >
                      Delete
                    </button>
                  )}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
