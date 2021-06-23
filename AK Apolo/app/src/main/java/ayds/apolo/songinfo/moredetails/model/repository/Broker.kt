package ayds.apolo.songinfo.moredetails.model.repository
import ayds.apolo.songinfo.moredetails.model.entities.Card

interface IBroker{
    fun getCards(artistName:String):List<Card>
}

internal class Broker(
    private var cards: MutableList<Card>,
    private var proxyLastFM: LastFMProxy
):IBroker{
    override fun getCards(artistName:String): List<Card> = cards

    override fun addCard(card : Card) {
        cards.add(card)
    }
}