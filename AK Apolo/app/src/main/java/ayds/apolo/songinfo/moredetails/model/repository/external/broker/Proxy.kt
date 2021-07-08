package ayds.apolo.songinfo.moredetails.model.repository.external.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card

interface Proxy {
    fun getCard(artistName: String): Card
}



