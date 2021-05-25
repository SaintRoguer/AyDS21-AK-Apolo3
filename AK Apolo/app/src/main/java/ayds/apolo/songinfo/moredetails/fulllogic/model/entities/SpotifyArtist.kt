package ayds.apolo.songinfo.moredetails.fulllogic.model.entities

interface Artist {
    val artistName: String
    val artistSpotifyUrl: String
    val artistImageUrl: String
    val artistMoreDetailsUrl : String
}

data class SpotifyArtist(
    override val artistName: String,
    override val artistSpotifyUrl: String,
    override val artistImageUrl: String,
    override val artistMoreDetailsUrl: String
) : Artist