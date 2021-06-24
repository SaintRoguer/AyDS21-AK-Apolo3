package ayds.apolo.songinfo.moredetails.model.repository
import ayds.apolo.songinfo.moredetails.model.entities.Card

interface IBroker{
    fun getCards(artistName : String): List<Card>
}

internal class Broker(
    private var cards: MutableList<Card>,
    private var nameOfTheArtist : String,
    private var proxyLastFM : LastFMProxy,
    private var proxyNewYork : NewYorkProxy,
):IBroker {

    override fun getCards(artistName : String): List<Card> {
        nameOfTheArtist = artistName
        searchAllCards()
        return cards
    }

    private fun searchAllCards(){
        searchLastFMCard()
        searchNyTimesCard()
        searchWikipediaCard()
    }

    private fun searchLastFMCard(){
        cards.add(proxyLastFM.mapCard(nameOfTheArtist))
    }

    private fun searchNyTimesCard(){
        cards.add(proxyNewYork.mapCard(nameOfTheArtist))
    }

    private fun searchWikipediaCard(){

    }
}