package ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.sqldb

const val ARTISTS_TABLE = "artists"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val ARTIST_DESC_COLUMN = "artist DESC"
const val NAME_DATABASE = "dictionary.db"
const val VERSION_DATABASE = 1
const val ARTICLE_URL_COLUMN= "article url"

const val createArtistsTableQuery: String =
    "create table $ARTISTS_TABLE (" +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN string, " +
            "$ARTIST_DESC_COLUMN, " +
            "$NAME_DATABASE, " +
            "$ARTICLE_URL_COLUMN, " +
            "$VERSION_DATABASE)"
