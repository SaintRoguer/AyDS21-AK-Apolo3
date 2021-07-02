package ayds.apolo.songinfo.moredetails.view

import ayds.apolo.songinfo.moredetails.model.entities.Card

data class MoreDetailsUiState(
    val artistName: String = "",
    val actionsEnabled: Boolean = true,
    var cards: List<Card> = listOf(),
    val sourceLogo : String = ""
) {

    companion object {
        const val IMAGE_NO_RESULTS_URL =
            "https://i.pinimg.com/originals/6a/6d/11/6a6d1124cf69e5588588bc7e397598f6.png"
    }
}