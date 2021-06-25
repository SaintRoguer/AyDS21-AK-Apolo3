package ayds.apolo.songinfo.moredetails.model.repository.local

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.FullCard

interface CardLocalStorage {

    fun saveCards(artistName : String, cards : List<Card>)

    fun getCards(artistName: String): List<Card>
}