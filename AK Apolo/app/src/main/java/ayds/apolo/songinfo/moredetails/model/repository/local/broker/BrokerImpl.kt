package ayds.apolo.songinfo.moredetails.model.repository.local.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.FullCard

interface Broker {
    fun getCards(artistName: String): List<Card>
}

internal class BrokerImpl(
    private val proxies: List<Proxy>
) : Broker {

    private var cards: MutableList<Card> = mutableListOf()

    override fun getCards(artistName: String): List<Card> {
        proxies.forEach {
            if(it.getCard(artistName) is FullCard){
                cards.add(it.getCard(artistName))
            }
        }
        return cards
    }
}