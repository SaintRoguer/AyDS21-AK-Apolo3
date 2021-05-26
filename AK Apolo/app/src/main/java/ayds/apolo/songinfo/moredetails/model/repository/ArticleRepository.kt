package ayds.apolo.songinfo.moredetails.model.repository

import android.util.Log
import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist
import ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.LastFMService
import ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.ArtistLocalStorage

interface ArticleRepository {
    fun getArticleByArtistName(artistName: String): ArticleArtist
}

internal class ArticleRepositoryImpl(
    private val ArtistLocalStorage: ArtistLocalStorage,
    private val LastFMService: LastFMService,
): ArticleRepository{
    override fun getArticleByArtistName(artistName: String): ArticleArtist{
        var artistArticle = ArtistLocalStorage.getArticleByArtistName(artistName)

        when{
            artistArticle != null -> markArticleAsLocal(artistArticle)
            else ->{
                try {
                    artistArticle = LastFMService.getArtistInfo(artistName)

                    artistArticle.let {
                        when {
                            it.isSavedArticle() -> ArtistLocalStorage.updateArtist(artistName, it.artistInfo)
                            else -> ArtistLocalStorage.insertArtist(artistName, it.artistInfo)
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

    private fun ArticleArtist.isSavedArticle() = ArtistLocalStorage.getArticleByArtistName(artistName) != null

    private fun markArticleAsLocal(artistArticle: ArticleArtist) {
        artistArticle.isLocallyStoraged = true
    }
}
