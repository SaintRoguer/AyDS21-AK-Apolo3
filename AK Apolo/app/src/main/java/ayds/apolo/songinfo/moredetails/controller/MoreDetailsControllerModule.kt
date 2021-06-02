package ayds.apolo.songinfo.moredetails.controller

import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.view.MoreDetailsView

object MoreDetailsControllerModule {
    fun viewStart(moreDetailsView: MoreDetailsView) {
        MoreDetailsControllerImpl(MoreDetailsModelModule.getMoreDetailsModel()).apply {
            setMoreDetailsView(moreDetailsView)
        }
    }
}