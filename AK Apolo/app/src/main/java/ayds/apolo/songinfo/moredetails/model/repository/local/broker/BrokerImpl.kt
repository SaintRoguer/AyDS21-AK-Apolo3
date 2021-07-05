package ayds.apolo.songinfo.moredetails.model.repository.local.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.FullCard

interface Broker {
    fun getCards(artistName: String): List<Card>
}

internal class BrokerImpl(
    private val proxies: List<Proxy>
) : Broker {

    override fun getCards(artistName: String): List<Card> =
         mutableListOf<Card>().apply{
            proxies.forEach {
                if (it.getCard(artistName) is FullCard) {
                    add(it.getCard(artistName))
                }
            }
        }
}