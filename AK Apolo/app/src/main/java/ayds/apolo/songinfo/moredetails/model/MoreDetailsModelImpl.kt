package ayds.apolo.songinfo.moredetails.model

import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel{

    fun searchArticle(artistName: String)

    fun songObservable(): Observable<ArticleArtist>

}

internal class MoreDetailsModelImpl(private val repository: ArticleRepository) : MoreDetailsModel{

    private val articleSubject= Subject<ArticleArtist>()

    override fun searchArticle(artistName: String){
        repository.getArticleByArtistName(artistName)
    }

    override fun songObservable(): Observable<ArticleArtist> = articleSubject

}