package ayds.apolo.songinfo.moredetails.model.repository

import android.util.Log
import ayds.apolo.songinfo.moredetails.model.entities.*
import ayds.apolo3.lastfm.LastFMInfoService
import ayds.apolo.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.apolo3.lastfm.Article


interface CardRepository {
    fun getArticleByArtistName(artistName: String): Card
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val lastFMInfoService: LastFMInfoService,
) : CardRepository {

    override fun getArticleByArtistName(artistName: String): Card {
        var cardArticle = cardLocalStorage.getCard(artistName)
        when {

            cardArticle != null -> cardInLocalStorage(cardArticle)
            else -> {
                try {
                    val serviceCard = lastFMInfoService.getCardInfo(artistName)

                    serviceCard?.let {
                        cardArticle = initFullCard(it)
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

    private fun initFullCard(article: Article) =
        FullCard(
            article.description,
            article.infoURL,
            Source.LAST_FM,
            article.sourceLogoURL
        )
}
