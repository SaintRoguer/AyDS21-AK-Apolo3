package ayds.apolo.songinfo.home.view

import ayds.apolo.songinfo.home.model.entities.Song

interface SongToReleaseDateFactory{
    fun get(song: Song) : SongToReleaseDateStringMapper
}

internal class SongToReleaseDateFactoryImpl : SongToReleaseDateFactory{

    override fun get(song: Song): SongToReleaseDateStringMapper =
        when (song.releaseDatePrecision){
            "day" -> SongToDayReleaseDateStringMapper(song)
            "month" -> SongToMonthReleaseDateStringMapper(song)
            "year" -> SongToYearReleaseDateStringMapper(song)
            else -> SongReleaseDateNotFound()
        }
}