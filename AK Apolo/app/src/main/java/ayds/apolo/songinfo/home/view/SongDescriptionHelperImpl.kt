package ayds.apolo.songinfo.home.view

import android.os.Build
import androidx.annotation.RequiresApi
import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import java.time.format.DateTimeFormatter

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStoraged) "[*]" else ""
                } \n" +
                        "Artist: ${song.artistName}  \n" +
                        "Album: ${song.albumName}  \n" +
                        "Release Date: ${parsingDateWithPrecision(song.releaseDatePrecision,song.releaseDate)}\n "

            else -> "Song not found"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parsingDateWithPrecision(precision:String, releaseDate:String)=when{
                                            (precision)=="day"->day(releaseDate)
                                            (precision)=="month"->month(releaseDate)
                                            (precision)=="year"->year(releaseDate)
                                            else ->"An error has occurred when modifying the format of the release date"

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun day(releaseDate :String): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
        return releaseDate.format(formatter)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun month(releaseDate :String): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM-uuuu")
        return releaseDate.format(formatter)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun year(releaseDate :String): String {
        val formatter = DateTimeFormatter.ofPattern("uuuu")
        val year = releaseDate.format(formatter)
        val leap = isLeapYear(year.toInt())
        return if (!leap) "$year (not a leap year)" else year
    }

    private fun isLeapYear(year:Int):Boolean=when {
                                                year % 4 == 0 -> {
                                                    when {
                                                        year % 100 == 0 -> year % 400 == 0
                                                        else -> true
                                                    }
                                                }
                                                else -> false
    }




}