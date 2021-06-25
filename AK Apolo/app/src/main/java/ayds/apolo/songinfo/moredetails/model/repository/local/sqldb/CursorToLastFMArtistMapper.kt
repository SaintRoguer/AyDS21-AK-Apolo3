package ayds.apolo.songinfo.moredetails.model.repository.local.sqldb

import android.database.Cursor
import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.EmptyCard
import ayds.apolo.songinfo.moredetails.model.entities.FullCard
import ayds.apolo.songinfo.moredetails.model.entities.Source
import java.sql.SQLException

interface CursorToLastFMArtistMapper {

    fun map(cursor: Cursor, cards: MutableList<Card>): List<Card>
}

internal class CursorToLastFMSongMapperImpl : CursorToLastFMArtistMapper {

    override fun map(cursor: Cursor, cards: MutableList<Card>): List<Card> {
        try {
            with(cursor) {
                if (moveToFirst()) {
                    do {
                        var cursorSource = Source.values()[getInt(
                            getColumnIndexOrThrow(
                                SOURCE_COLUMN
                            ))]
                        when {
                             cursorSource != Source.NO ->
                                cards.add(
                                    FullCard(
                                        description = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                                        infoURL = getString(getColumnIndexOrThrow(CARD_URL_COLUMN)),
                                        source = Source.values()[getInt(
                                            getColumnIndexOrThrow(
                                                SOURCE_COLUMN
                                            )
                                        )],
                                        sourceLogoURL = getString(
                                            getColumnIndexOrThrow(
                                                SOURCE_LOGO_COLUMN
                                            )
                                        )
                                    )
                                )
                            else -> EmptyCard
                        }
                    } while(moveToNext())
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
        return cards
    }
}