package ayds.apolo.songinfo.moredetails.model

import android.util.Log
import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel{

    fun searchArticle(artistName: String)

    fun articleObservable(): Observable<ArticleArtist>

}

internal class MoreDetailsModelImpl(private val repository: ArticleRepository) :
    MoreDetailsModel {

    private val articleSubject= Subject<ArticleArtist>()

    override fun searchArticle(artistName: String){
        repository.getArticleByArtistName(artistName).let{
            Log.e("LastFM Article", "model notify $it")
            articleSubject.notify(it)
        }
    }

    override fun articleObservable(): Observable<ArticleArtist> = articleSubject

}