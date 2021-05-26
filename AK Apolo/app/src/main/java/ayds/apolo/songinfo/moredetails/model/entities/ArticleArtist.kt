package ayds.apolo.songinfo.moredetails.model.entities

data class ArticleArtist(
    val artistName: String,
    var artistInfo: String,
    val artistURL: String,
    var isLocallyStoraged: Boolean
)