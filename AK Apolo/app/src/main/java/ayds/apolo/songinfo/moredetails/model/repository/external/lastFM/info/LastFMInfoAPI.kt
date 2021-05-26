package ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.info

import retrofit2.http.*
import retrofit2.Call

internal interface LastFMInfoAPI {

    @GET("?method=artist.getinfo&api_key=0a657854db69e551c97d541ca9ebcef4&format=json")
    fun getArtistInfo(@Query("artist") artist: String):  Call<String>
}