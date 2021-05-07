package ayds.apolo.songinfo.home.view

import ayds.apolo.songinfo.home.model.entities.Song

interface SongToReleaseDateStringMapper {

    fun map() : String
}

internal class SongToDayReleaseDateStringMapper (private val song: Song) : SongToReleaseDateStringMapper{

    private fun getDayPrecision(releaseDate : String): String {
        val dateYearMonthDay = releaseDate.split("-")
        val day = dateYearMonthDay[2]
        val month = dateYearMonthDay[1]
        val year = dateYearMonthDay[0]
        return "$day/$month/$year"
    }

    override fun map(): String {
        return getDayPrecision(song.releaseDate)
    }
}

internal class SongToMonthReleaseDateStringMapper (private val song: Song) : SongToReleaseDateStringMapper{

    private fun getStringMonth(numberMonth: String) =
        when (numberMonth) {
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "August"
            "09" -> "September"
            "10" -> "October"
            "11" -> "November"
            "12" -> "December"
            else -> throw IllegalArgumentException(numberMonth)
        }

    private fun getMonthPrecision(releaseDate : String): String {
        val dateYearMonth = releaseDate.split("-")
        val monthNumber = dateYearMonth[1]
        val monthName = getStringMonth(monthNumber)
        val year = dateYearMonth[0]
        return "$monthName,$year"
    }

    override fun map(): String {
        return getMonthPrecision(song.releaseDate)
    }
}

internal class SongToYearReleaseDateStringMapper (private val song: Song) : SongToReleaseDateStringMapper{

    private fun isLeapYear(year: Int) = (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))

    private  fun getYearPrecision(year : String): String {
        return when(isLeapYear(year.toInt())) {
            false -> "$year (not a leap year)"
            else -> year
        }
    }

    override fun map(): String {
        return getYearPrecision(song.releaseDate)
    }
}

internal class SongReleaseDateNotFound (private val song: Song): SongToReleaseDateStringMapper{

    override fun map(): String {
        return song.releaseDate
    }
}