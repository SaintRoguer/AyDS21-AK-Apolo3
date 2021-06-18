package ayds.apolo.songinfo.moredetails.view

import ayds.apolo.songinfo.moredetails.model.entities.Source

data class MoreDetailsUiState(
    val artistName: String = "",
    val articleURL: String = "",
    val cardInfo: String = "",
    var sourceLogoURL: String = "",
    val sourceLabel: Source=Source.NO
)




