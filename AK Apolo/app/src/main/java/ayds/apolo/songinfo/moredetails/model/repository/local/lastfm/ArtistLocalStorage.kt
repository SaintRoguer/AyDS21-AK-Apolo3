package ayds.apolo.songinfo.moredetails.model.repository.local.lastfm

import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle

interface ArtistLocalStorage {

    fun saveArticle(artist: String, info: String)

    fun getArticleByArtistName(artistName: String): ArtistArticle?
}