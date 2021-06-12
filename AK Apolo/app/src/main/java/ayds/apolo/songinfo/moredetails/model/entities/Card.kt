package ayds.apolo.songinfo.moredetails.model.entities

interface Card {
    var description: String
    var infoURL: String
    var source: Source
    var sourceLogoURL: String
}

open class FullCard(
    override var description: String = "",
    override var infoURL: String = "",
    override var source: Source = Source.NO,
    override var sourceLogoURL: String = ""
) : Card

open class EmptyCard(
    override var description: String = "",
    override var infoURL: String = "",
    override var source: Source = Source.NO,
    override var sourceLogoURL: String = ""
) : Card


