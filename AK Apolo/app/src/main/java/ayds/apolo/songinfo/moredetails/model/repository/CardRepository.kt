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

        if(!isInLocalStorage){
            cardLocalStorage.saveCards(artistName, broker.getCards(artistName))
        }
        else{
            cardsArticles.forEach {
                setStorage(it)
            }
        }

        return cardsArticles //Chequear si son toda la lista esta vacia.
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
