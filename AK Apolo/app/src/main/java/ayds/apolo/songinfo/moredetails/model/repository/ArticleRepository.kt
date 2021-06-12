package ayds.apolo.songinfo.moredetails.model.repository

import android.util.Log
import ayds.apolo.songinfo.moredetails.model.entities.*
import ayds.apolo3.lastfm.LastFMInfoService
import ayds.apolo3.lastfm.ArtistArticle as ServiceLastFMData
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.ArtistLocalStorage


interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): Card
}

internal class ArticleRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMInfoService:  LastFMInfoService,
) : ArticleRepository {

    override fun getArticleByArtistName(artistName: String): Card {
        var cardArticle = artistLocalStorage.getArticleByArtistName(artistName)
        when {
            cardArticle != null -> articleInLocalStorage(cardArticle)
            else -> {
                try {
                    val serviceLastFMData = lastFMInfoService.getArtistInfo(artistName)

                    serviceLastFMData?.let{
                        cardArticle = FullCard(
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
        return cardArticle ?: EmptyCard
    }

    private fun articleInLocalStorage(artistArticle: ArtistArticle) {
        artistArticle.isLocallyStoraged=true
    }
}
