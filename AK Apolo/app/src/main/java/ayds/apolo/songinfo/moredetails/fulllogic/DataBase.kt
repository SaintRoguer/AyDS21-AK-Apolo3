package ayds.apolo.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor;
import android.content.ContentValues
import android.content.Context
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.ArrayList

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable =
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        db.execSQL(createTable)
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}


    companion object {
        const val TimeOutInSec = 30

        fun createDatabase() {
            var DatabaseConnection: Connection? = null
            try {
                DatabaseConnection = getConnection()
                initializeDatabase(DatabaseConnection)
            } catch (e: SQLException) {
                System.err.println(e.message)
            } finally {
                try {
                    DatabaseConnection?.close()
                } catch (e: SQLException) {
                    System.err.println(e)
                }
            }
        }

        fun getConnection(): Connection {
            return DriverManager.getConnection("jdbc:sqlite:./dictionary.db")
        }


        private fun initializeDatabase(DatabaseConnection: Connection?) {
            val statement = DatabaseConnection?.createStatement()
            if (statement != null) {
                statement.queryTimeout = TimeOutInSec
            }
            statement?.executeQuery("select * from artists")?.let { fillDatabase(resultSet = it) }
        }

        private fun fillDatabase(resultSet: java.sql.ResultSet) {
            while (resultSet.next()) {
                println("id = " + resultSet.getInt("id"))
                println("artist = " + resultSet.getString("artist"))
                println("info = " + resultSet.getString("info"))
                println("source = " + resultSet.getString("source"))
            }
        }

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val databaseToModify = dbHelper.writableDatabase
            fillDatabaseWithNewColumn(ContentValues(), artist, info)
            databaseToModify.insert("artists", null, ContentValues())
        }

        private fun fillDatabaseWithNewColumn(
            databaseNewColumn: ContentValues,
            artist: String?,
            info: String?
        ) {
            databaseNewColumn.put("artist", artist)
            databaseNewColumn.put("info", info)
            databaseNewColumn.put("source", 1)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val cursor = newArtistCursor(dbHelper, artist)
            val items = getArtistItems(dbHelper, artist, cursor)
            cursor.close()
            return when {
                items.isEmpty() -> null
                else -> items[0]
            }
        }

        private fun newArtistCursor(dbHelper: DataBase, artist: String): Cursor {
            val db = dbHelper.readableDatabase
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

        private fun getArtistItems(
            dbHelper: DataBase,
            artist: String,
            cursor: Cursor
        ): MutableList<String> {
            var items: MutableList<String> = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow("info")
                )
                items.add(info)
            }
            return items
        }


    }

}