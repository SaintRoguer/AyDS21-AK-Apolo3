package ayds.apolo.songinfo.moredetails.fulllogic.view

import ayds.apolo.songinfo.moredetails.fulllogic.controller.MoreDetailsControllerModule
import ayds.apolo.songinfo.moredetails.fulllogic.model.MoreDetailsModelModule

object MoreDetailsViewModule {
    //private val SongToReleaseDateFactory : SongToReleaseDateFactory = SongToReleaseDateFactoryImpl()
    //val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(SongToReleaseDateFactory)

    fun init(moreDetailsView: MoreDetailsView){
        MoreDetailsModelModule.initMoreDetailsModel(moreDetailsView)
        MoreDetailsControllerModule.viewStart(moreDetailsView)
    }
}