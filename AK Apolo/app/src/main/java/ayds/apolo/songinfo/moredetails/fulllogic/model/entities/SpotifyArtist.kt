package ayds.apolo.songinfo.moredetails.fulllogic.model.entities

interface Artist {
    val id:String
    val artistName: String
    val artistSource: String
    val artistInfo: String
    val artistDesc : String
}

data class SpotifyArtist(
    override val id: String,
    override val artistName: String,
    override val artistSource: String,
    override val artistInfo: String,
    override val artistDesc: String
) : Artist