package ayds.apolo.songinfo.moredetails.model

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.repository.CardRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {

    fun searchCard(artistName: String)

    fun cardObservable(): Observable<Card>
}

internal class MoreDetailsModelImpl(private val repository: CardRepository) :
    MoreDetailsModel {

    private val cardSubject = Subject<Card>()

    override fun searchCard(artistName: String) {
        repository.getArticleByArtistName(artistName).let {
            cardSubject.notify(it)
        }
    }

    override fun cardObservable(): Observable<Card> = cardSubject
}