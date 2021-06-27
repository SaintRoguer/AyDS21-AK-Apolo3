package ayds.apolo.songinfo.moredetails.model

import android.content.Context
import ayds.apolo.songinfo.moredetails.model.repository.CardRepository
import ayds.apolo.songinfo.moredetails.model.repository.CardRepositoryImpl
import ayds.apolo.songinfo.moredetails.model.repository.local.CardLocalStorage
import ayds.apolo.songinfo.moredetails.model.repository.local.broker.Broker
import ayds.apolo.songinfo.moredetails.model.repository.local.broker.LastFMProxy
import ayds.apolo.songinfo.moredetails.model.repository.local.broker.NewYorkProxy
import ayds.apolo.songinfo.moredetails.model.repository.local.sqldb.CardLocalStorageImpl
import ayds.apolo.songinfo.moredetails.view.MoreDetailsView
import ayds.apolo.songinfo.moredetails.model.repository.local.sqldb.CursorToLastFMSongMapperImpl
import ayds.apolo3.lastfm.LastFMModule
import ayds.jkhera2.nytimes.NYTModule

object MoreDetailsModelModule {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {

        val newYorkProxy = NewYorkProxy(NYTModule.nytArticleService)
        val lastFMProxy = LastFMProxy(LastFMModule.lastFMInfoService)
        val broker = Broker(listOf(lastFMProxy, newYorkProxy))

        val artistLocalStorage: CardLocalStorage = CardLocalStorageImpl(
            moreDetailsView as Context, CursorToLastFMSongMapperImpl()
        )

        val repository: CardRepository =
            CardRepositoryImpl(artistLocalStorage, broker)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}