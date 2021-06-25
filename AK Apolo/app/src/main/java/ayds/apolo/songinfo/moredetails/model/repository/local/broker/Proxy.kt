package ayds.apolo.songinfo.moredetails.model.repository.local.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card

interface Proxy {
    fun getCard(artistName: String, cards: MutableList<Card>): MutableList<Card>
}



