package ayds.apolo.songinfo.home.view

import android.os.Build
import androidx.annotation.RequiresApi
import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import java.util.*

interface ReleaseDatePrecision {

    fun parsingDateWithPrecision(precision: String, releaseDate: String) : String
}

internal class ReleaseDatePrecisionImpl : ReleaseDatePrecision{
    override fun parsingDateWithPrecision(precision: String, releaseDate: String) : String{
        val dateYearMonthDay = releaseDate.split("-")
        return when {
            precision == "day" && checkReleaseDateDay(dateYearMonthDay) -> getDayPrecision(dateYearMonthDay)
            precision == "month" && checkReleaseDateMonth(dateYearMonthDay) -> getMonthPrecision(dateYearMonthDay)
            precision == "year" && checkReleaseDateYear(dateYearMonthDay) -> getYearPrecision(dateYearMonthDay)
            else -> return "An error has occurred when modifying the format of the release date"
        }
    }

    private fun checkReleaseDateYear(dateYearMonthDay : List<String>) : Boolean {
        return dateYearMonthDay[0].toInt() in 0..Calendar.getInstance().get(Calendar.YEAR)
    }

    private fun checkReleaseDateDay(dateYearMonthDay : List<String>) : Boolean{
        return dateYearMonthDay[2].toInt() in 1..31 &&
                checkReleaseDateMonth(dateYearMonthDay)
    }

    private fun checkReleaseDateMonth(dateYearMonthDay : List<String>) : Boolean {
        return dateYearMonthDay[1].toInt() in 1..12 &&
                checkReleaseDateYear(dateYearMonthDay)
    }

    private fun getDayPrecision(dateYearMonthDay : List<String>): String {
        return dateYearMonthDay[2] + "/" + dateYearMonthDay[1] + "/" + dateYearMonthDay[0]
    }

    private fun getMonthPrecision(dateYearMonthDay : List<String>): String {
        return getStringMonth(dateYearMonthDay[1]) + ", " + dateYearMonthDay[0]
    }

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
                else -> null
            }

    private  fun getYearPrecision(dateYearMonthDay: List<String>): String {
        val year = dateYearMonthDay[0]
        val res_isLeap = isLeapYear(year.toInt())
        return if(!res_isLeap) year + " (not a leap year)" else year
    }

    private fun isLeapYear(year: Int): Boolean = when {
        year % 4 == 0 -> {
            when {
                year % 100 == 0 -> year % 400 == 0
                else -> true
            }
        }
        else -> false
    }


}
