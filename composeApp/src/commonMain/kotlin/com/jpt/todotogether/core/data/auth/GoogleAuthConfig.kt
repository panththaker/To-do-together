package com.jpt.todotogether.core.data.auth

// The Web OAuth Client ID from Google Cloud Console - passed to KMPAuth as
// the "serverId". It's a public identifier (not a secret) shared by every
// platform; the backend uses the same ID as the audience when verifying the
// ID tokens KMPAuth produces.
object GoogleAuthConfig {
    const val WEB_CLIENT_ID = "169498662906-iuam010s30l11b6f9qmqh0jtck0peta8.apps.googleusercontent.com"

    // Desktop-only: KMPAuth's loopback OAuth listener. Must be registered as
    // an Authorized redirect URI for the Web client above in Google Cloud
    // Console - Google rejects unregistered redirect URIs.
    const val DESKTOP_REDIRECT_URI = "http://localhost:8123/callback"
}
