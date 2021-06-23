package ayds.apolo.songinfo.moredetails.model.repository.local

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.FullCard

interface CardLocalStorage {

    fun saveCard(artistName : String, card : Card)

    fun getCard(artistName: String): FullCard?
}