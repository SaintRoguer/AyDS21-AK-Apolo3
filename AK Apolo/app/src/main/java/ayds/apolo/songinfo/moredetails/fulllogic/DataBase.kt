package ayds.apolo.songinfo.moredetails.fulllogic

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import ayds.apolo.songinfo.moredetails.fulllogic.DataBase
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
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)")
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
                FillDatabase(resulSet)
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

        private fun FillDatabase(resultSet: java.sql.ResultSet) {
            while (resultSet.next()) {
                println("id = " + resultSet.getInt("id"))
                println("artist = " + resultSet.getString("artist"))
                println("info = " + resultSet.getString("info"))
                println("source = " + resultSet.getString("source"))
            }
        }

        @JvmStatic
        fun saveArtist(dbHelper: DataBase, artist: String?, info: String?) {
            val MutableDatabase = dbHelper.writableDatabase

            val databaseNewColumn= ContentValues()
            FillDatabaseWithNewColumn(databaseNewColumn,artist,info)
            MutableDatabase.insert("artists", null, databaseNewColumn)
        }

        private fun FillDatabaseWithNewColumn(databaseNewColumn:ContentValues,artist:String?,info:String?){
            databaseNewColumn.put("artist", artist)
            databaseNewColumn.put("info", info)
            databaseNewColumn.put("source", 1)
        }

        @JvmStatic
        fun getInfo(dbHelper: DataBase, artist: String): String? {
            val db = dbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
            val projection = arrayOf(
                "id",
                "artist",
                "info"
            )

// Filter results WHERE "title" = 'My Title'
            val selection = "artist  = ?"
            val selectionArgs = arrayOf(artist)

// How you want the results sorted in the resulting Cursor
            val sortOrder = "artist DESC"
            val cursor = db.query(
                "artists",  // The table to query
                projection,  // The array of columns to return (pass null to get all)
                selection,  // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,  // don't group the rows
                null,  // don't filter by row groups
                sortOrder // The sort order
            )
            val items: MutableList<String> = ArrayList()
            while (cursor.moveToNext()) {
                val info = cursor.getString(
                    cursor.getColumnIndexOrThrow("info"))
                items.add(info)
            }
            cursor.close()
            return if (items.isEmpty()) null else items[0]
        }
    }

}