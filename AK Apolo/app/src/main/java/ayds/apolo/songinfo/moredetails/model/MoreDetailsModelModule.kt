package ayds.apolo.songinfo.moredetails.model

import android.content.Context
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepository
import ayds.apolo.songinfo.moredetails.model.repository.ArticleRepositoryImpl
import ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.LastFMInfoService
import ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.LastFMModule
import ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.ArtistLocalStorage
import ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.sqldb.ArtistLocalStorageImpl
import ayds.apolo.songinfo.moredetails.view.MoreDetailsView
import ayds.apolo.songinfo.moredetails.model.repository.local.lastFM.sqldb.CursorToLastFMSongMapperImpl

object MoreDetailsModelModule {

    private lateinit var moreDetailsModel : MoreDetailsModel

    fun getMoreDetailsModel() : MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView : MoreDetailsView){
        val artistLocalStorage: ArtistLocalStorage = ArtistLocalStorageImpl(
            moreDetailsView as Context, CursorToLastFMSongMapperImpl()
        )
        val lastFMInfoService: LastFMInfoService = LastFMModule.lastFMInfoService

        val repository: ArticleRepository =
            ArticleRepositoryImpl(artistLocalStorage, lastFMInfoService)

        moreDetailsModel= MoreDetailsModelImpl(repository)
    }

}