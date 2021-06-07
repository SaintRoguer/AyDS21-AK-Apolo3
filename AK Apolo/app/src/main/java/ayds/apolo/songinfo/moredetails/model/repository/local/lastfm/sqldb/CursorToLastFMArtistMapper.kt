package ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb

import android.database.Cursor
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import java.sql.SQLException

interface CursorToLastFMArtistMapper {

    fun map(cursor: Cursor): ArtistArticle?
}

internal class CursorToLastFMSongMapperImpl : CursorToLastFMArtistMapper {

    override fun map(cursor: Cursor): ArtistArticle? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    ArtistArticle(
                        articleInfo = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        articleURL = getString(getColumnIndexOrThrow(SOURCE_COLUMN))
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