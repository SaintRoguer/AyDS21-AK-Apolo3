package ayds.apolo.songinfo.moredetails.model.repository

import android.util.Log
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import ayds.apolo.songinfo.moredetails.model.entities.Article
import ayds.apolo.songinfo.moredetails.model.entities.EmptyArticle
import ayds.apolo.songinfo.moredetails.model.repository.external.lastfm.LastFMInfoService
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.ArtistLocalStorage

const val STORE_LETTER = "*"

interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): Article
}

internal class ArticleRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val lastFMInfoService: LastFMInfoService,
) : ArticleRepository {

    override fun getArticleByArtistName(artistName: String): Article {
        var artistArticle = artistLocalStorage.getArticleByArtistName(artistName)
        when {
            artistArticle != null -> artistArticle.isLocallyStoraged = true
            else -> {
                try {
                    artistArticle = lastFMInfoService.getArtistInfo(artistName)
                    artistArticle?.let {
                        artistLocalStorage.saveArticle(artistName, it)
                    }
                } catch (e: Exception) {
                    Log.e("Artist Article", "ERROR: $e")
                }
            }
        }
        return artistArticle ?: EmptyArticle
    }
}
