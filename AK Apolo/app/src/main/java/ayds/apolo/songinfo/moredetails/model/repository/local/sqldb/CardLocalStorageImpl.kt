package ayds.apolo.songinfo.moredetails.model.repository.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.FullCard
import ayds.apolo.songinfo.moredetails.model.repository.local.CardLocalStorage

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "cards.db"

internal class CardLocalStorageImpl(
    context: Context,
    private val cursorToLastFMArtistMapper: CursorToLastFMArtistMapper,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    CardLocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        CARD_COLUMN,
        INFO_COLUMN,
        CARD_URL_COLUMN,
        SOURCE_COLUMN,
        SOURCE_LOGO_COLUMN,
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun saveCard(artistName: String, card: Card) {
        val column = fillDatabaseWithNewRow(artistName,card)
        this.writableDatabase.insert(CARDS_TABLE, null, column)
    }

    private fun fillDatabaseWithNewRow(artistName: String, card : Card) =
        ContentValues().apply {
            put(CARD_COLUMN, artistName)
            put(INFO_COLUMN, card.description)
            put(CARD_URL_COLUMN, card.infoURL)
            put(SOURCE_COLUMN, card.source.ordinal)
            put(SOURCE_LOGO_COLUMN, card.sourceLogoURL)
        }

    override fun getCard(artistName: String): FullCard? {
        val cursor = readableDatabase.query(
            CARDS_TABLE,
            projection,
            "$CARD_COLUMN = ?",
            arrayOf(artistName),
            null,
            null,
            CARD_DESC_COLUMN
        )
        return cursorToLastFMArtistMapper.map(cursor)
    }
}