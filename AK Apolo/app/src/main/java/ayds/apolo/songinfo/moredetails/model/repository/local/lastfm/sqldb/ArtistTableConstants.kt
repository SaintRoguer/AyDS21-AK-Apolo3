package ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb

const val ARTISTS_TABLE = "artists"
const val ARTIST_COLUMN = "artist"
const val ID_COLUMN = "id"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val ARTICLE_URL_COLUMN = "article_url"
const val ARTIST_DESC_COLUMN = "artist DESC"

const val createArtistsTableQuery: String =
    "create table $ARTISTS_TABLE (" +
            "$ARTIST_COLUMN string, " +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$INFO_COLUMN string, " +
            "$ARTICLE_URL_COLUMN string"+
            "$SOURCE_COLUMN integer)"