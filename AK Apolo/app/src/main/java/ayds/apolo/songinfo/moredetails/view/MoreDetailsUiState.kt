package ayds.apolo.songinfo.moredetails.view

import ayds.apolo.songinfo.moredetails.model.entities.Card

data class MoreDetailsUiState(
    val artistName: String = "",
    var cards: List<Card> = listOf()
)




