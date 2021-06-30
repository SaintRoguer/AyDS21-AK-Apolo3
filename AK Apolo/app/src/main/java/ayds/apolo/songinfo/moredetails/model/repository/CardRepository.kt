package ayds.apolo.songinfo.moredetails.model.repository

import ayds.apolo.songinfo.moredetails.model.entities.*
import ayds.apolo.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.apolo.songinfo.moredetails.model.repository.local.broker.Broker


interface CardRepository {
    fun getArticleByArtistName(artistName: String): List<Card>
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : CardRepository {

    private var isInLocalStorage = false

    override fun getArticleByArtistName(artistName: String): List<Card> {
        val cardsArticles = cardLocalStorage.getCards(artistName)

        if (!isInLocalStorage) {
            cardLocalStorage.saveCards(artistName, broker.getCards(artistName))
        } else {
            cardsArticles.forEach {
                setStorage(it)
            }
        }

        return when (checkAnyFullCard(cardsArticles)) {
            true -> cardsArticles
            false -> listOf()
        }
    }

    private fun setStorage(cardArticle: Card) {
        if (cardArticle is FullCard) {
            cardInLocalStorage(cardArticle)
            isInLocalStorage = true
        }
    }

    private fun cardInLocalStorage(cardArticle: Card) {
        cardArticle.isLocallyStoraged = true
    }

    private fun checkAnyFullCard(cardsArticles: List<Card>): Boolean {
        var allFullCards = false
        for (cardsArticle in cardsArticles) {
            if (cardsArticle is FullCard)
                allFullCards = true
        }
        return allFullCards
    }

}
