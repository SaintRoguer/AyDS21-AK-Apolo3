package ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.info

import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.LastFMInfoService
import retrofit2.Response

internal class LastFmInfoServiceImpl(
    private val lastFMInfoAPI: LastFMInfoAPI,
    private val lastFMToInfoResolver: LastFMToInfoResolver
):LastFMInfoService {


    override fun getArtistInfo(artist: String): ArtistArticle? {
        val callResponse = getResponseFromService(artist)
        return lastFMToInfoResolver.getInfoFromExternalData(callResponse.body())
    }

    private fun getResponseFromService(artistName: String): Response<String> =
        lastFMInfoAPI.getArtistInfo(artistName).execute()
}