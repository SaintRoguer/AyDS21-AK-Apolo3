package ayds.apolo.songinfo.moredetails.model.entities


enum class Source(val service: String) {
    LAST_FM("Last FM"),
    NEW_YORK_TIMES("New York Times"),
    WIKIPEDIA("Wikipedia"),
    NO("None"),
    NO_RESULTS("No results")
}
