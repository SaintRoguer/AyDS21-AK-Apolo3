package ayds.apolo.songinfo.moredetails.model.entities

interface Article {
    val artistName: String
    val articleInfo: String
    val articleURL: String
    var isLocallyStoraged: Boolean
}

data class ArtistArticle(
    override val artistName: String,
    override var articleInfo: String,
    override val articleURL: String,
    override var isLocallyStoraged: Boolean = false
) : Article

object EmptyArticle : Article {
    override val artistName: String = ""
    override val articleInfo: String = ""
    override val articleURL: String = ""
    override var isLocallyStoraged: Boolean = false
}