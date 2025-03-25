package pl.lambada.songsync.data.remote.lyrics_providers.spotify

import android.os.Build
import android.util.Log
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import pl.lambada.songsync.domain.model.SongInfo
import pl.lambada.songsync.domain.model.lyrics_providers.spotify.SyncedLinesResponse
import pl.lambada.songsync.util.networking.Ktor.client
import pl.lambada.songsync.util.networking.Ktor.json

class SpotifyLyricsAPI {
    private val baseURL = "https://lyrichub.vercel.app/api/spotify"

    /**
     * Gets synced lyrics using the song link and returns them as a string formatted as an LRC file.
     * @param title The title of the song.
     * @param artist The name of the artist.
     * @return The synced lyrics as a string.
     */
    suspend fun getSyncedLyrics(title: String, artist: String): String? {
        val response = client.get(baseURL) {
            parameter("query", "$title $artist")
        }
        val responseBody = response.bodyAsText(Charsets.UTF_8)
        if (response.status.value !in 200..299)
            return null

        val json = json.decodeFromString<SyncedLinesResponse>(responseBody)

        if (json.lyrics == "Not Found.")
            return null

        return json.lyrics
    }
}