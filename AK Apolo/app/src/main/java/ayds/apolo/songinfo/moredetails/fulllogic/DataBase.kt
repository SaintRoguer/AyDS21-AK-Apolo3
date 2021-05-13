package ayds.apolo.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor
import android.content.ContentValues
import android.content.Context
import java.util.ArrayList

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun saveArtist(artist: String, info: String) {
        val databaseToModify = this.writableDatabase
        val databaseNewRow = ContentValues()
        fillDatabaseWithNewRow(databaseNewRow, artist, info)
        databaseToModify.insert("artists", null, databaseNewRow)
    }

    private fun fillDatabaseWithNewRow(databaseNewColumn: ContentValues,artist: String,info: String){
        databaseNewColumn.put("artist", artist)
        databaseNewColumn.put("info", info)
        databaseNewColumn.put("source", 1)
    }

    fun getInfo(artist: String): String? {
        val cursor: Cursor = newArtistCursor(artist)
        val items = getArtistItems(cursor)
        cursor.close()
        return when {
            items.isEmpty() -> null
            else -> items[0]
        }
    }

    private fun newArtistCursor(artist: String): Cursor {
        val db = this.readableDatabase
        val projection = arrayOf("id", "artist", "info")
        val selection = "artist  = ?"
        val selectionArgs = arrayOf(artist)
        val sortOrder = "artist DESC"
        return db.query(
            "artists",
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
            val columnIndex = cursor.getColumnIndexOrThrow("info")
            val info = cursor.getString(columnIndex)
            items.add(info)
        }
        return items
    }

}