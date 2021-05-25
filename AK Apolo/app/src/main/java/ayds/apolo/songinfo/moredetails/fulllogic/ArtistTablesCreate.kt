package ayds.apolo.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.content.ContentValues
import android.content.Context
import java.util.ArrayList

class ArtistTablesCreate(context: Context) : SQLiteOpenHelper(context, NAME_DATABASE, null, VERSION_DATABASE) {

    private val projection = arrayOf(INFO_COLUMN, ARTIST_COLUMN, INFO_COLUMN)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(onCreateString)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun saveArtist(artist: String, info: String) {
        val artistInfoArray = arrayOf(artist, info)
        val column=fillDatabaseWithNewRow(artistInfoArray)
        this.writableDatabase.insert(ARTISTS_COLUMN, null, column)
    }

    private fun fillDatabaseWithNewRow(artistInfo: Array<String>) = ContentValues().apply{
        put(ARTIST_COLUMN, artistInfo[0])
        put(INFO_COLUMN, artistInfo[1])
        put(SOURCE_COLUMN, 1)
    }

    fun getInfo(artist: String): String? {
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
            ARTISTS_COLUMN,
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