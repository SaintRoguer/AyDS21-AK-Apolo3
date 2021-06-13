package ayds.apolo.songinfo.moredetails.model

import android.content.Context
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepository
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepositoryImpl
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.CardLocalStorage
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb.CardLocalStorageImpl
import ayds.apolo.songinfo.moredetails.view.MoreDetailsView
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb.CursorToLastFMSongMapperImpl

object MoreDetailsModelModule {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val artistLocalStorage: CardLocalStorage = CardLocalStorageImpl(
            moreDetailsView as Context, CursorToLastFMSongMapperImpl()
        )
        val lastFMInfoService: ayds.apolo3.lastfm.LastFMInfoService = ayds.apolo3.lastfm.LastFMModule.lastFMInfoService

        val repository: ArticleRepository =
            ArticleRepositoryImpl(artistLocalStorage, lastFMInfoService)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}