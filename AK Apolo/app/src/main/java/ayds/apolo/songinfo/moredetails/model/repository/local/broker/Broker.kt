package ayds.apolo.songinfo.moredetails.model.repository.local.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card

interface IBroker {
    fun getCards(artistName: String): List<Card>
}

internal class Broker(
    private val proxies: List<Proxy>
) : IBroker {

    private var cards: MutableList<Card> = mutableListOf()

    override fun getCards(artistName: String): List<Card> {
        proxies.forEach {
            cards.add(it.getCard(artistName))
        }
        return cards
    }
}