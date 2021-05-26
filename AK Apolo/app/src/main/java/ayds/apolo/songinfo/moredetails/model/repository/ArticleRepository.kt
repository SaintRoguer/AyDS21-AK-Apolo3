package ayds.apolo.songinfo.moredetails.model.repository

import android.util.Log
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist
import ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.LastFMService
import ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.ArtistLocalStorage

interface ArticleRepository {
    fun getArticleByTerm(term: String): ArticleArtist
}

internal class ArticleRepositoryImpl(
    private val ArtistLocalStorage: ArtistLocalStorage,
    private val LastFMService: LastFMService,
): ArticleRepository{
    override fun getArticleByTerm(artistName: String): ArticleArtist{
        var artistArticle = ArtistLocalStorage.getArticleByTerm(artistName)

        when{
            artistArticle != null -> markArticleAsLocal(artistArticle)
            else ->{
                try {
                    artistArticle = LastFMService.getArtistInfo(artistName)

                    artistArticle.let {
                        when {
                            it.isSavedArticle() -> ArtistLocalStorage.updateArtist(artistName, it.id)
                            else -> ArtistLocalStorage.insertArtist(artistName, it)
                        }
                    }
                } catch(e: Exception){
                    Log.e("Artist Article","ERROR: $e")
                }
            }
        }
        Log.e("Artist Article", "Article $artistArticle")
        return artistArticle
    }

    private fun ArticleArtist.isSavedArticle() = ArtistLocalStorage.getArticleByTerm(artistName) != null

    private fun markArticleAsLocal(artistArticle: SpotifySong) {
        artistArticle.isLocallyStoraged = true
    }
}
