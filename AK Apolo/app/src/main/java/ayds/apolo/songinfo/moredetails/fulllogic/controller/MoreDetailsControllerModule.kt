package ayds.apolo.songinfo.moredetails.fulllogic.controller

import ayds.apolo.songinfo.home.controller.HomeControllerImpl
import ayds.apolo.songinfo.home.model.HomeModelModule
import ayds.apolo.songinfo.home.view.HomeView
import ayds.apolo.songinfo.moredetails.fulllogic.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.fulllogic.view.MoreDetailsView

object MoreDetailsControllerModule {

    fun viewStart(moreDetailsView: MoreDetailsView) {
        MoreDetailsControllerImpl(MoreDetailsModelModule.getMoreDetailsModel()).apply {
            setMoreDetailsView(moreDetailsView)
        }
    }
}