package ayds.apolo.songinfo.moredetails.model.repository.local.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card

interface IBroker {
    fun getCards(artistName: String): List<Card>
}

internal class Broker(
    private val proxies: List<Proxy>,
) : IBroker {

    private lateinit var cards: MutableList<Card>

    override fun getCards(artistName: String): List<Card> {
        proxies.forEach {
            val card = it.getCard(artistName)
            cards.add(card)
        }
        return cards
    }
}