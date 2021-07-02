package ayds.apolo.songinfo.moredetails.model.entities

import ayds.apolo.songinfo.moredetails.view.MoreDetailsUiState
import ayds.apolo.songinfo.moredetails.view.MoreDetailsUiState.Companion.IMAGE_NO_RESULTS_URL

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

object NoResultsCard: Card {
    override var description: String = "No results"
    override var infoURL: String = ""
    override var source : Source = Source.NO_RESULTS
    override var sourceLogoURL : String = IMAGE_NO_RESULTS_URL
    override var isLocallyStoraged : Boolean = false

}



