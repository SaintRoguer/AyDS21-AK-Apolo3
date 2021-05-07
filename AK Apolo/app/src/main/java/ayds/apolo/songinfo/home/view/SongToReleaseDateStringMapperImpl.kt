package ayds.apolo.songinfo.home.view

import ayds.apolo.songinfo.home.model.entities.Song

interface SongToReleaseDateStringMapper {

    fun map() : String
}

internal class SongToDayReleaseDateStringMapper (private val song: Song) : SongToReleaseDateStringMapper{

    private fun getDayPrecision(dateYearMonthDay : List<String>): String {
        return dateYearMonthDay[2] + "/" + dateYearMonthDay[1] + "/" + dateYearMonthDay[0]
    }

    override fun map(): String {
        return getDayPrecision(song.releaseDate.split("-"))
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

    private fun getMonthPrecision(dateYearMonthDay : List<String>): String {
        return getStringMonth(dateYearMonthDay[1]) + ", " + dateYearMonthDay[0]
    }

    override fun map(): String {
        return getMonthPrecision(song.releaseDate.split("-"))
    }
}

internal class SongToYearReleaseDateStringMapper (private val song: Song) : SongToReleaseDateStringMapper{

    private fun isLeapYear(year: Int) = (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))

    private  fun getYearPrecision(dateYearMonthDay: List<String>): String {
        val year = dateYearMonthDay[0]
        val res_isLeap = isLeapYear(year.toInt())
        return if(!res_isLeap) year + " (not a leap year)" else year
    }

    override fun map(): String {
        return getYearPrecision(song.releaseDate.split("-"))
    }
}

internal class SongReleaseDateNotFound : SongToReleaseDateStringMapper{

    override fun map(): String {
        return "--/--/----"
    }
}