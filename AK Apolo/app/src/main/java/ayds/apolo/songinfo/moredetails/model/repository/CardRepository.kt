package ayds.apolo.songinfo.moredetails.model.repository

import ayds.apolo.songinfo.moredetails.model.entities.*
import ayds.apolo.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.apolo.songinfo.moredetails.model.repository.local.broker.Broker


interface CardRepository {
    fun getArticleByArtistName(artistName: String): List<Card>
}

internal class CardRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker : Broker
) : CardRepository {

    private var isInLocalStorage = false

    override fun getArticleByArtistName(artistName: String): List<Card> {
        val cardsArticles = cardLocalStorage.getCards(artistName)

        cardsArticles.forEach {
            setStorage(it)
        }

        if(!isInLocalStorage){
            cardLocalStorage.saveCards(artistName, broker.getCards(artistName))
        }

        return cardsArticles
    }

    private fun setStorage (cardArticle : Card) {
        if (cardArticle is FullCard) {
            cardInLocalStorage(cardArticle)
            isInLocalStorage = true
        }
    }

    private fun cardInLocalStorage(cardArticle : Card) {
        cardArticle.isLocallyStoraged = true
    }
}
