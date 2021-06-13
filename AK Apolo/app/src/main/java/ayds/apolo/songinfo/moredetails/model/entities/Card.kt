package ayds.apolo.songinfo.moredetails.model.entities

interface Card {
    var description: String
    var infoURL: String
    var source: Int
    var sourceLogoURL: String
    var isLocallyStoraged: Boolean
}

data class FullCard(
    override var description: String = "",
    override var infoURL: String = "",
    override var source: Int,
    override var sourceLogoURL: String = "",
    override var isLocallyStoraged: Boolean= false
) : Card

object EmptyCard: Card {
    override var description: String = ""
    override var infoURL: String = ""
    override var source: Int = 0
    override var sourceLogoURL: String = ""
    override var isLocallyStoraged: Boolean= false
}


