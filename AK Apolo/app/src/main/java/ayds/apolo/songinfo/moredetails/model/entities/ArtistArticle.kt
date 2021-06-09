package ayds.apolo.songinfo.moredetails.model.entities

interface Article {
    val articleInfo: String
    val articleURL: String
    var isLocallyStoraged: Boolean
}

data class ArtistArticle(
    override var articleInfo: String,
    override val articleURL: String,
    override var isLocallyStoraged: Boolean = false
) : Article

object EmptyArticle : Article {
    override val articleInfo: String = ""
    override val articleURL: String = ""
    override var isLocallyStoraged: Boolean = false
}