package ayds.apolo.songinfo.moredetails.model

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {

    fun searchArticle(artistName: String)

    fun articleObservable(): Observable<Card>
}

internal class MoreDetailsModelImpl(private val repository: ArticleRepository) :
    MoreDetailsModel {

    private val cardSubject = Subject<Card>()

    override fun searchArticle(artistName: String) {
        repository.getArticleByArtistName(artistName).let {
            cardSubject.notify(it)
        }
    }

    override fun articleObservable(): Observable<Card> = cardSubject
}