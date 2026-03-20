import { useState, useEffect, useCallback, useRef } from "react"
import { useAuth } from "../context/AuthContext.tsx"
import { Link } from "react-router-dom"

const API_BASE = "http://localhost:8080"

type Post = {
  id: string
  imageUrl: string
  caption: string
  username: string
  createdAt: string
}

export default function CommunityPage() {
  const { token, username, isLoggedIn } = useAuth()
  const [posts, setPosts] = useState<Post[]>([])
  const [loading, setLoading] = useState(true)
  const [showUpload, setShowUpload] = useState(false)
  const [uploading, setUploading] = useState(false)
  const [caption, setCaption] = useState("")
  const [preview, setPreview] = useState<string | null>(null)
  const fileRef = useRef<HTMLInputElement>(null)
  const [selectedFile, setSelectedFile] = useState<File | null>(null)

  const fetchPosts = useCallback(async () => {
    try {
      const res = await fetch(`${API_BASE}/api/community`)
      if (res.ok) setPosts(await res.json())
    } catch {}
    finally { setLoading(false) }
  }, [])

  useEffect(() => { fetchPosts() }, [fetchPosts])

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (!file) return
    setSelectedFile(file)
    const reader = new FileReader()
    reader.onload = () => setPreview(reader.result as string)
    reader.readAsDataURL(file)
  }

  const handleUpload = async () => {
    if (!selectedFile) return
    setUploading(true)
    try {
      const form = new FormData()
      form.append("image", selectedFile)
      form.append("caption", caption)
      const res = await fetch(`${API_BASE}/api/community`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
        body: form,
      })
      if (res.ok) {
        setShowUpload(false)
        setCaption("")
        setPreview(null)
        setSelectedFile(null)
        fetchPosts()
      }
    } finally { setUploading(false) }
  }

  const handleDelete = async (id: string) => {
    await fetch(`${API_BASE}/api/community/${id}`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${token}` },
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
    <div className="min-h-screen bg-neutral-950">
      <div className="sticky top-0 bg-neutral-950/95 backdrop-blur-sm border-b border-neutral-800 z-20">
        <div className="max-w-7xl mx-auto px-6 py-3 flex items-center justify-between">
          <Link to="/" className="text-xl font-bold text-white">FishingPal</Link>
          <h1 className="text-lg font-semibold text-white">Community</h1>
          <div className="flex items-center gap-3">
            {isLoggedIn && (
              <button
                onClick={() => setShowUpload(true)}
                className="px-4 py-1.5 bg-white text-black text-sm font-medium rounded-full
                  hover:bg-neutral-200 transition"
              >
                + Share Photo
              </button>
            )}
            {isLoggedIn ? (
              <span className="text-sm text-neutral-400">{username}</span>
            ) : (
              <Link to="/login" className="text-sm text-white font-medium">Sign In</Link>
            )}
          </div>
        </div>
      </div>

      {showUpload && (
        <div className="fixed inset-0 bg-black/70 z-50 flex items-center justify-center p-4">
          <div className="bg-neutral-900 rounded-2xl border border-neutral-800 w-full max-w-md p-6 space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-lg font-semibold text-white">Share a photo</h2>
              <button onClick={() => { setShowUpload(false); setPreview(null); setSelectedFile(null); setCaption("") }}
                className="text-neutral-500 hover:text-white text-xl">&times;</button>
            </div>

            {preview ? (
              <img src={preview} alt="Preview" className="w-full h-56 object-cover rounded-xl" />
            ) : (
              <button
                onClick={() => fileRef.current?.click()}
                className="w-full h-56 border-2 border-dashed border-neutral-700 rounded-xl
                  flex items-center justify-center text-neutral-500 hover:border-neutral-500 transition"
              >
                Click to select image
              </button>
            )}
            <input ref={fileRef} type="file" accept="image/*" onChange={handleFileChange} className="hidden" />

            {preview && (
              <button onClick={() => { setPreview(null); setSelectedFile(null) }}
                className="text-xs text-neutral-500 hover:text-white">Change image</button>
            )}

            <textarea
              value={caption}
              onChange={(e) => setCaption(e.target.value)}
              placeholder="Add a caption..."
              maxLength={300}
              rows={2}
              className="w-full resize-none bg-neutral-800 border border-neutral-700 rounded-lg p-3
                text-sm text-white placeholder-neutral-500 focus:outline-none focus:border-neutral-500"
            />

            <button
              onClick={handleUpload}
              disabled={!selectedFile || uploading}
              className="w-full py-2.5 bg-white text-black text-sm font-medium rounded-lg
                hover:bg-neutral-200 transition disabled:opacity-40"
            >
              {uploading ? "Uploading..." : "Share"}
            </button>
          </div>
        </div>
      )}

      <div className="max-w-7xl mx-auto px-6 py-8">
        {loading && (
          <div className="flex justify-center py-20">
            <div className="animate-spin rounded-full h-6 w-6 border-2 border-white border-t-transparent"></div>
          </div>
        )}

        {!loading && posts.length === 0 && (
          <p className="text-center text-neutral-500 py-20 text-sm">
            No photos yet. {isLoggedIn ? "Be the first to share!" : ""}
          </p>
        )}

        {!loading && posts.length > 0 && (
          <div className="columns-2 md:columns-3 lg:columns-4 gap-3">
            {posts.map((post) => (
              <div
                key={post.id}
                className="relative group rounded-2xl overflow-hidden mb-3 break-inside-avoid"
              >
                <img
                  src={`${API_BASE}${post.imageUrl}`}
                  alt={post.caption || "Community photo"}
                  className="w-full h-auto block
                    group-hover:scale-105 transition-transform duration-500"
                />

                  <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/10 to-transparent" />

                  <div className="absolute bottom-0 left-0 right-0 p-4">
                    {post.caption && (
                      <p className="text-white font-semibold text-sm leading-snug drop-shadow-lg">
                        {post.caption}
                      </p>
                    )}
                    <p className="text-white/60 text-xs mt-1">
                      {post.username} &middot; {timeAgo(post.createdAt)}
                    </p>
                  </div>

                  {isLoggedIn && post.username === username && (
                    <button
                      onClick={(e) => { e.stopPropagation(); handleDelete(post.id) }}
                      className="absolute top-3 right-3 w-7 h-7 bg-black/60 text-white rounded-full
                        flex items-center justify-center text-xs opacity-0 group-hover:opacity-100 transition
                        hover:bg-red-600"
                    >
                      &times;
                    </button>
                  )}
                </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
