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



    override fun getArticleByArtistName(artistName: String): List<Card> {
        val cardsArticles = cardLocalStorage.getCards(artistName)
        var isInLocalStorage = false

        cardsArticles.forEach {
            if (it is FullCard) {
                cardInLocalStorage(it)
                isInLocalStorage = true
            }
        }
        if(!isInLocalStorage){
            val serviceCards = broker.getCards(artistName)
            cardLocalStorage.saveCards(artistName, serviceCards)
        }
        return cardsArticles
    }

    private fun cardInLocalStorage(cardArticle: Card) {
        cardArticle.isLocallyStoraged = true
    }
}
