package ayds.apolo.songinfo.moredetails.fulllogic.model.repository.local_spotify.sqldb

import android.database.Cursor
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.ID_COLUMN
import ayds.apolo.songinfo.moredetails.fulllogic.model.entities.SpotifyArtist
import java.sql.SQLException

interface CursorToSpotifyArtistMapper {

    fun map(cursor: Cursor): SpotifyArtist?
}

internal class CursorToSpotifySongMapperImpl : CursorToSpotifyArtistMapper {

    override fun map(cursor: Cursor): SpotifyArtist? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    SpotifyArtist(
                        id = getString(getColumnIndexOrThrow(ID_COLUMN)),
                        artistName = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        artistInfo = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        artistDesc = getString(getColumnIndexOrThrow(ARTIST_DESC_COLUMN)),
                        artistSource = getString(getColumnIndexOrThrow(SOURCE_COLUMN))
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
}