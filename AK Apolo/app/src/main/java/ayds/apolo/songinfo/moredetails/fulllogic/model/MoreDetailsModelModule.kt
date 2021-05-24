package ayds.apolo.songinfo.moredetails.fulllogic.model

import ayds.apolo.songinfo.moredetails.fulllogic.view.MoreDetailsViewActivity

object MoreDetailsModelModule {

    private lateinit var moreDetailsModel : MoreDetailsModel

    fun getMoreDetailsModel() : MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView : MoreDetailsViewActivity){

    }

}