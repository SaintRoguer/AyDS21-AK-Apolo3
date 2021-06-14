package ayds.apolo.songinfo.moredetails.model.entities

interface Card {
    var description: String
    var infoURL: String
    var source : Source
    var sourceLogoURL: String
    var isLocallyStoraged: Boolean
}

data class FullCard(
    override var description: String = "",
    override var infoURL: String = "",
    override var source : Source = Source.NO,
    override var sourceLogoURL: String = "",
    override var isLocallyStoraged: Boolean= false
) : Card

object EmptyCard: Card {
    override var description: String = ""
    override var infoURL: String = ""
    override var source : Source = Source.NO
    override var sourceLogoURL: String = ""
    override var isLocallyStoraged: Boolean= false
}


