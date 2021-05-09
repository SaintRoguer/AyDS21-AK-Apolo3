package ayds.apolo.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor;
import android.content.ContentValues
import android.content.Context
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.ArrayList

class DataBase(context: Context?) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        db.execSQL(createTable)
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        const val TimeOutInSec = 30

        fun generateDatabase() {
            var DatabaseConnection: Connection? = null
            try {
                DatabaseConnection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db")
                val resulSet=createStatement(DatabaseConnection)
                fillDatabase(resulSet)
            } catch (e: SQLException) {
                // Tener en cuenta que si no se dispone del espacio suficiente,
                // no se crea la base de datos y tira la excepcion
                System.err.println(e.message)
            } finally {
                try {
                    DatabaseConnection?.close()
                } catch (e: SQLException) {
                    System.err.println(e)
                }
            }
        }

        private fun createStatement(DatabaseConnection: Connection): ResultSet {
            val statement = DatabaseConnection.createStatement()
            statement.queryTimeout = DataBase.TimeOutInSec
            val resultSet = statement.executeQuery("select * from artists")
            return resultSet
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
            val mutableDatabase = dbHelper.writableDatabase
            fillDatabaseWithNewColumn(ContentValues(),artist,info)
            mutableDatabase.insert("artists", null, ContentValues())
        }

        private fun fillDatabaseWithNewColumn(databaseNewColumn:ContentValues, artist:String?, info:String?){
            databaseNewColumn.put("artist", artist)
            databaseNewColumn.put("info", info)
            databaseNewColumn.put("source", 1)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val cursor = newArtistCursor(dbHelper,artist)
            val items = getArtistItems(dbHelper,artist,cursor)
            cursor.close()
            return when {
                items.isEmpty() -> null
                else -> items[0]
            }
        }

        private fun newArtistCursor(dbHelper: DataBase, artist: String) : Cursor {
            val db = dbHelper.readableDatabase
            val projection = arrayOf("id","artist","info")
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

        private fun getArtistItems(dbHelper: DataBase, artist: String, cursor : Cursor) : MutableList<String> {
            var items: MutableList<String>  = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow("info"))
                items.add(info)
            }
            return items
        }


    }

}