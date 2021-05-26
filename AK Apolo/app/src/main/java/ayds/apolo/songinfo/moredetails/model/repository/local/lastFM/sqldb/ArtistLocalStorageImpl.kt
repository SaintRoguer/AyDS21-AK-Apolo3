package ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.ArtistLocalStorage
import java.util.ArrayList

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "artists.db"

internal class ArtistLocalStorageImpl(
    context: Context,
    private val cursorToSpotifyArtistMapper: CursorToSpotifyArtistMapper,
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {

    private val projection = arrayOf(
        ARTIST_COLUMN,
        SOURCE_COLUMN,
        INFO_COLUMN,
        ARTIST_DESC_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun saveArtist(artist: String, info: String) {
        val artistInfoArray = arrayOf(artist, info)
        val column=fillDatabaseWithNewRow(artistInfoArray)
        this.writableDatabase.insert(ARTISTS_TABLE, null, column)
    }

    private fun fillDatabaseWithNewRow(artistInfo: Array<String>) = ContentValues().apply{
        put(ARTIST_COLUMN, artistInfo[0])
        put(INFO_COLUMN, artistInfo[1])
        put(SOURCE_COLUMN, 1)
    }

    override fun getInfo(artist: String): String? {
        val cursor: Cursor = newArtistCursor(artist)
        val items = getArtistItems(cursor)
        cursor.close()
        return items.firstOrNull()
    }

    private fun newArtistCursor(artist: String): Cursor {
        val selection = "artist  = ?"
        val selectionArgs = arrayOf(artist)
        val sortOrder = ARTIST_DESC_COLUMN
        return this.readableDatabase.query(
            ARTISTS_TABLE,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
    }

    private fun getArtistItems(cursor: Cursor): MutableList<String> {
        val items = ArrayList<String>()
        while (cursor.moveToNext()) {
            val columnIndex = cursor.getColumnIndexOrThrow(INFO_COLUMN)
            val info = cursor.getString(columnIndex)
            items.add(info)
        }
        return items
    }

}