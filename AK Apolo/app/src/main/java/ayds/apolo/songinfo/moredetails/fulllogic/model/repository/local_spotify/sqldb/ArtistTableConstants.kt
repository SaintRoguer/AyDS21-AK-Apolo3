package ayds.apolo.songinfo.moredetails.fulllogic.model.repository.local_spotify.sqldb

const val ARTISTS_COLUMN = "artists"
const val ARTIST_COLUMN = "artist"
const val ID_COLUMN = "id"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val ARTIST_DESC_COLUMN = "artist DESC"
const val NAME_DATABASE = "dictionary.db"
const val VERSION_DATABASE = 1

const val onCreateString: String =
    "create table $ARTISTS_COLUMN (" +
            "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN string)"