package ayds.apolo.songinfo.moredetails.view

import ayds.apolo.songinfo.moredetails.controller.MoreDetailsControllerModule
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule

object MoreDetailsViewModule {
    //private val SongToReleaseDateFactory : SongToReleaseDateFactory = SongToReleaseDateFactoryImpl()
    //val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(SongToReleaseDateFactory)

    fun init(moreDetailsView: MoreDetailsView){
        MoreDetailsModelModule.initMoreDetailsModel(moreDetailsView)
        MoreDetailsControllerModule.viewStart(moreDetailsView)
    }
}