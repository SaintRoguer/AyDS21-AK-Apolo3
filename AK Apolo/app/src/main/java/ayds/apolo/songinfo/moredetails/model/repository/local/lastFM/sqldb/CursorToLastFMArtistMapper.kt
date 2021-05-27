package ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.sqldb

import android.database.Cursor
import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist
import java.sql.SQLException

interface CursorToLastFMArtistMapper {

    fun map(cursor: Cursor): ArticleArtist?
}

internal class CursorToLastFMSongMapperImpl : CursorToLastFMArtistMapper {

    override fun map(cursor: Cursor): ArticleArtist? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    ArticleArtist(
                        artistName = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        artistInfo = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        artistURL = getString(getColumnIndexOrThrow(ARTICLE_URL_COLUMN))
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