package ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.FullCard
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.CardLocalStorage

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "artists.db"

internal class CardLocalStorageImpl(
    context: Context,
    private val cursorToLastFMArtistMapper: CursorToLastFMArtistMapper,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    CardLocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN,
        CARD_URL_COLUMN,
        SOURCE_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun saveCard(artistName : String, card : Card) {
        val column = fillDatabaseWithNewRow(artistName, card.description, card.infoURL, card.source, card.sourceLogoURL)
        this.writableDatabase.insert(ARTISTS_TABLE, null, column)
    }

    private fun fillDatabaseWithNewRow(artistName : String, description: String, infoURL : String, source : Int, sourceLogoURL : String) =
        ContentValues().apply {
            put(ARTIST_COLUMN, artistName)
            put(INFO_COLUMN, description)
            put(CARD_URL_COLUMN, infoURL)
            put(SOURCE_COLUMN, 1)
            put(SOURCE_LOGO_COLUMN, sourceLogoURL)
    }

    override fun getCard(artistName: String): FullCard? {
        val cursor = readableDatabase.query(
            ARTISTS_TABLE,
            projection,
            "$ARTIST_COLUMN = ?",
            arrayOf(artistName),
            null,
            null,
            ARTIST_DESC_COLUMN
        )
        return cursorToLastFMArtistMapper.map(cursor)
    }
}