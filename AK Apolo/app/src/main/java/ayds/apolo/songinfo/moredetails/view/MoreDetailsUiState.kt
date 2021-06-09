package ayds.apolo.songinfo.moredetails.view

data class MoreDetailsUiState(
    val artistName: String = "",
    val articleURL: String = "",
    val artistInfo: String = ""
){

    companion object {
        const val IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}

