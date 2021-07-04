package ayds.apolo.songinfo.moredetails.view

import ayds.apolo.songinfo.moredetails.model.entities.Card

data class MoreDetailsUiState(
    val artistName: String = "",
    val actionsEnabled: Boolean = true,
    var cards: List<Card> = listOf(),
    val sourceLogo: String = "",
    var indexSpinner: Int = 0
) {
    fun getCurrentCard(): Card = cards[indexSpinner]

    companion object {
        const val IMAGE_NO_RESULTS_URL =
            "https://i.imgur.com/97QT1wn.png"
    }
}