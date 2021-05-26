package ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.info

import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist
import ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.LastFMService
import retrofit2.Response

internal class LastFmServiceImpl(
    private val lastFMInfoAPI: LastFMInfoAPI
    private val lastFMToInfoResolver: LastFMToInfoResolver
):LastFMService {


    override fun getArtistInfo(artist: String): ArticleArtist {
        val callResponse = getResponseFromService(artist);
        return lastFMToInfoResolver.getInfoFromExternalData(callResponse.body())
    }

    private fun getResponseFromService(artistName: String): Response<String> =
        lastFMInfoAPI.getArtistInfo(artistName).execute()







}