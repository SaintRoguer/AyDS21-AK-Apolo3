package ayds.apolo.songinfo.moredetails.fulllogic.model

import ayds.apolo.songinfo.moredetails.fulllogic.model.repository.local_spotify.ArtistLocalStorage
import ayds.apolo.songinfo.moredetails.fulllogic.view.MoreDetailsView

object MoreDetailsModelModule {

    private lateinit var moreDetailsModel : MoreDetailsModel

    fun getMoreDetailsModel() : MoreDetailsModel = moreDetailsModel

    fun initMoreDetailsModel(moreDetailsView : MoreDetailsView){
        //
    }

}