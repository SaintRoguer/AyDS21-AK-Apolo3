package ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.ArtistLocalStorage

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "artists.db"

internal class ArtistLocalStorageImpl(
    context: Context,
    private val cursorToLastFMArtistMapper: CursorToLastFMArtistMapper,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {

    private val projection = arrayOf(
        ARTIST_COLUMN,
        ID_COLUMN,
        SOURCE_COLUMN,
        INFO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun saveArticle(artist: String, info: String) {
        val artistInfoArray = arrayOf(artist, info)
        val column=fillDatabaseWithNewRow(artistInfoArray)
        this.writableDatabase.insert(ARTISTS_TABLE, null, column)
    }

    private fun fillDatabaseWithNewRow(artistInfo: Array<String>) = ContentValues().apply{
        put(ARTIST_COLUMN, artistInfo[0])
        put(INFO_COLUMN, artistInfo[1])
        put(SOURCE_COLUMN, 1)
    }

    override fun getArticleByArtistName(artistName: String): ArtistArticle? {
        val cursor= readableDatabase.query(
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