package ayds.apolo.songinfo.moredetails.fulllogic.model.repository.local_spotify.sqldb

const val ARTISTS_TABLE = "artists"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val ARTIST_DESC_COLUMN = "artist DESC"
const val NAME_DATABASE = "dictionary.db"
const val VERSION_DATABASE = 1

const val createArtistsTableQuery: String =
    "create table $ARTISTS_TABLE (" +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN string, " +
            "$ARTIST_DESC_COLUMN, " +
            "$NAME_DATABASE, " +
            "$VERSION_DATABASE)"
