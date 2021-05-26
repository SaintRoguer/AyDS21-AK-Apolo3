package ayds.apolo.songinfo.moredetails.model

import ayds.apolo.songinfo.moredetails.view.MoreDetailsView

object MoreDetailsModelModule {

    private lateinit var moreDetailsModel : MoreDetailsModel

    fun getMoreDetailsModel() : MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView : MoreDetailsView){
        //Aca se usa el lastFMInfoService del LastFMModule
    }

}