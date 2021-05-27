package ayds.apolo.songinfo.moredetails.model.entities

interface Article {
    val artistName: String
    val artistInfo: String
    val artistURL: String
    var isLocallyStoraged: Boolean
}

data class ArticleArtist(
    override val artistName: String,
    override var artistInfo: String,
    override val artistURL: String,
    override var isLocallyStoraged: Boolean = false
) : Article

object EmptyArticle : Article{
    override val artistName : String = ""
    override val artistInfo : String = ""
    override val artistURL : String = ""
    override var isLocallyStoraged: Boolean = false
}