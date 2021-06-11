package ayds.apolo.songinfo.moredetails.model.repository

import android.util.Log
import ayds.apolo.songinfo.moredetails.model.entities.Article
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import ayds.apolo.songinfo.moredetails.model.entities.EmptyArticle
import ayds.apolo3.lastfm.LastFMInfoService
import ayds.apolo3.lastfm.ArtistArticle as ServiceLastFMData
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.ArtistLocalStorage


interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): Article
}

internal class ArticleRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMInfoService:  LastFMInfoService,
) : ArticleRepository {

    override fun getArticleByArtistName(artistName: String): Article {
        var artistArticle = artistLocalStorage.getArticleByArtistName(artistName)
        when {
            artistArticle != null -> articleInLocalStorage(artistArticle)
            else -> {
                try {
                    val serviceLastFMData = lastFMInfoService.getArtistInfo(artistName)

                    serviceLastFMData?.let{
                        artistArticle = ArtistArticle(
                            it.articleInfo,
                            it.articleURL,
                            it.isLocallyStoraged
                        )
                    }

                    artistArticle?.let {
                        artistLocalStorage.saveArticle(artistName, it)
                    }
                } catch (e: Exception) {
                    Log.e("Artist Article", "ERROR: $e")
                }
            }
        }
        return artistArticle ?: EmptyArticle
    }

    private fun articleInLocalStorage(artistArticle: ArtistArticle) {
        artistArticle.isLocallyStoraged=true
    }
}
