package ayds.apolo.songinfo.moredetails.model

import ayds.apolo.songinfo.moredetails.view.MoreDetailsView

object MoreDetailsModelModule {

    private lateinit var moreDetailsModelImpl : MoreDetailsModelImpl

    fun getMoreDetailsModel() : MoreDetailsModelImpl = moreDetailsModelImpl

    fun initMoreDetailsModel(moreDetailsView : MoreDetailsView){
        //
    }

}