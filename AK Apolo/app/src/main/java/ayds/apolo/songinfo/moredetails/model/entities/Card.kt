package ayds.apolo.songinfo.moredetails.model.entities

open class Card (
    var description : String = "",
    var infoURL : String = "",
    var source : Source = Source.NO,
    var sourceLogoURL : String = ""
 )

object EmptyCard : Card()
