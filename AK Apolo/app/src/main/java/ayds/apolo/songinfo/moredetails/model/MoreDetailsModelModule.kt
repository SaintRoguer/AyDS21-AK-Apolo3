package ayds.apolo.songinfo.moredetails.model

import android.content.Context
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepository
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepositoryImpl
import ayds.apolo.songinfo.moredetails.model.repository.external.lastfm.LastFMInfoService
import ayds.apolo.songinfo.moredetails.model.repository.external.lastfm.LastFMModule
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.ArtistLocalStorage
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb.ArtistLocalStorageImpl
import ayds.apolo.songinfo.moredetails.view.MoreDetailsView
import ayds.apolo.songinfo.moredetails.model.repository.local.lastfm.sqldb.CursorToLastFMSongMapperImpl

object MoreDetailsModelModule {

    private lateinit var moreDetailsModel: MoreDetailsModel

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView: MoreDetailsView) {
        val artistLocalStorage: ArtistLocalStorage = ArtistLocalStorageImpl(
            moreDetailsView as Context, CursorToLastFMSongMapperImpl()
        )
        val lastFMInfoService: LastFMInfoService = LastFMModule.lastFMInfoService

        val repository: ArticleRepository =
            ArticleRepositoryImpl(artistLocalStorage, lastFMInfoService)

        moreDetailsModel = MoreDetailsModelImpl(repository)
    }

}