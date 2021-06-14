package ayds.apolo.songinfo.moredetails.model.repository

import android.util.Log
import ayds.apolo.songinfo.moredetails.model.entities.*
import ayds.apolo3.lastfm.LastFMInfoService
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.CardLocalStorage


interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): Card
}

internal class ArticleRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val lastFMInfoService: LastFMInfoService,
) : ArticleRepository {

    override fun getArticleByArtistName(artistName: String): Card {
        var cardArticle = cardLocalStorage.getCard(artistName)
        when {

            cardArticle != null -> cardInLocalStorage(cardArticle)
            else -> {
                try {
                    val serviceCard = lastFMInfoService.getCardInfo(artistName)

                    serviceCard?.let {
                        cardArticle = FullCard(
                            it.description,
                            it.infoURL,
                            Source.LAST_FM,
                            it.sourceLogoURL
                        )
                    }
                    cardArticle?.let {
                        cardLocalStorage.saveCard(artistName, it)
                    }
                } catch (e: Exception) {
                    Log.e("Artist Article", "ERROR: $e")
                }
            }
        }
        return cardArticle ?: EmptyCard
    }

    private fun cardInLocalStorage(cardArticle: FullCard) {
        cardArticle.isLocallyStoraged = true
    }
}
