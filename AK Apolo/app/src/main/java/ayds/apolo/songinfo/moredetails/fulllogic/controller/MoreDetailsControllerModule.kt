package ayds.apolo.songinfo.moredetails.fulllogic.controller

import ayds.apolo.songinfo.moredetails.fulllogic.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.fulllogic.view.MoreDetailsViewActivity

object MoreDetailsControllerModule {

    fun viewStart(moreDetailsView: MoreDetailsViewActivity) {
        MoreDetailsControllerImpl(MoreDetailsModelModule.getMoreDetailsModel()).apply {
            setMoreDetailsView(moreDetailsView)
        }
    }
}