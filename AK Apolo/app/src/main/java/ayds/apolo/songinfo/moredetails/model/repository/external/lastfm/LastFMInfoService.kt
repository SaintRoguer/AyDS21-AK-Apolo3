package ayds.apolo.songinfo.moredetails.model.repository.external.lastfm

import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import retrofit2.http.Query

interface LastFMInfoService {
    fun getArtistInfo(@Query("artist") artist: String): ArtistArticle?
}