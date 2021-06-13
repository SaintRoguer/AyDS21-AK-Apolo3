package ayds.apolo.songinfo.moredetails.model.repository.local.lastfm

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.FullCard

interface CardLocalStorage {

    fun saveCard(artistName : String, card : Card)

    fun getCard(artistName: String): FullCard?
}