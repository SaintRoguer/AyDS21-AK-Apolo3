package ayds.apolo.songinfo.moredetails.model.repository

import ayds.apolo.songinfo.moredetails.model.entities.*
import ayds.apolo.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.apolo.songinfo.moredetails.model.repository.external.broker.BrokerImpl

interface CardRepository {
    fun getArticleByArtistName(artistName: String): List<Card>
}

private const val STORE_LETTER = "*\n\n"

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val brokerImpl: BrokerImpl
) : CardRepository {

    override fun getArticleByArtistName(artistName: String): List<Card> {
        var cardsArticles = cardLocalStorage.getCards(artistName)

        if (cardsArticles.isEmpty()) {
            cardsArticles = brokerImpl.getCards(artistName)
            cardLocalStorage.saveCards(artistName, cardsArticles)
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
        }
    }

    private fun cardInLocalStorage(cardArticle: Card) {
        cardArticle.isLocallyStoraged = true
        cardArticle.description = STORE_LETTER.plus(cardArticle.description)
    }

    private fun checkAnyFullCard(cardsArticles: List<Card>): Boolean =
        cardsArticles.any { it is FullCard }

}
