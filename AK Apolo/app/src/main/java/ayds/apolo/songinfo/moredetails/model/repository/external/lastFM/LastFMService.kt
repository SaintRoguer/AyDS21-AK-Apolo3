package ayds.apolo.songinfo.moredetails.model.repository.external.lastFM

import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFMService {

    @GET("?method=artist.getinfo&api_key=0a657854db69e551c97d541ca9ebcef4&format=json")
    fun getArtistInfo(@Query("artist") artist: String): ArticleArtist
}