package ayds.apolo.songinfo.moredetails.model.repository.local.lastfm

import ayds.apolo.songinfo.moredetails.model.entities.Article
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle

interface ArtistLocalStorage {

    fun saveArticle(artistName : String, article : Article)

    fun getArticleByArtistName(artistName: String): ArtistArticle?
}