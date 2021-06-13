package ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb

const val ARTISTS_TABLE = "artists"
const val ARTIST_COLUMN = "artist"
const val ID_COLUMN = "id"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val CARD_URL_COLUMN = "article_url"
const val ARTIST_DESC_COLUMN = "artist DESC"
const val SOURCE_LOGO_COLUMN = "source logo"

const val createArtistsTableQuery: String =
    "create table $ARTISTS_TABLE (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$CARD_URL_COLUMN string, "+
            "$SOURCE_COLUMN integer, "+
            "$SOURCE_LOGO_COLUMN string)"