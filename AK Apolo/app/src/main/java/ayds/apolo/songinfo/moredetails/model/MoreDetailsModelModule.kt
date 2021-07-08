package ayds.apolo.songinfo.moredetails.model

import android.content.Context
import ayds.apolo.songinfo.moredetails.model.repository.CardRepository
import ayds.apolo.songinfo.moredetails.model.repository.CardRepositoryImpl
import ayds.apolo.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.apolo.songinfo.moredetails.model.repository.external.broker.BrokerImpl
import ayds.apolo.songinfo.moredetails.model.repository.external.broker.LastFMProxy
import ayds.apolo.songinfo.moredetails.model.repository.external.broker.NewYorkProxy
import ayds.apolo.songinfo.moredetails.model.repository.external.broker.WikipediaProxy
import ayds.apolo.songinfo.moredetails.model.repository.local.sqldb.CardLocalStorageImpl
import ayds.apolo.songinfo.moredetails.view.MoreDetailsView
import ayds.apolo.songinfo.moredetails.model.repository.local.sqldb.CursorToLastFMSongMapperImpl
import ayds.apolo3.lastfm.LastFMModule
import ayds.jkhera2.nytimes.NYTModule
import ayds.zeus1.wikipedia.WikipediaModule

object MoreDetailsModelModule {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {

        val newYorkProxy = NewYorkProxy(NYTModule.nytArticleService)
        val lastFMProxy = LastFMProxy(LastFMModule.lastFMInfoService)
        val wikipediaProxy = WikipediaProxy(WikipediaModule.wikipediaService)
        val broker = BrokerImpl(listOf(lastFMProxy, newYorkProxy, wikipediaProxy))

        val artistLocalStorage: CardLocalStorage = CardLocalStorageImpl(
            moreDetailsView as Context, CursorToLastFMSongMapperImpl()
        )

        val repository: CardRepository =
            CardRepositoryImpl(artistLocalStorage, broker)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}