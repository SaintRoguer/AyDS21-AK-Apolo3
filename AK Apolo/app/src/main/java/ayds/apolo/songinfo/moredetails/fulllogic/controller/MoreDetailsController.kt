package ayds.apolo.songinfo.moredetails.fulllogic.controller

import ayds.apolo.songinfo.moredetails.fulllogic.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.fulllogic.view.MoreDetailsViewActivity


interface MoreDetailsController{
     fun setMoreDetailsView(moreDetailsView : MoreDetailsViewActivity)
}

internal class MoreDetailsControllerImpl(
    private val moreDetailsModel : MoreDetailsModel
) : MoreDetailsController{

    private lateinit var moreDetailsView : MoreDetailsViewActivity

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsViewActivity){
        this.moreDetailsView = moreDetailsView
       // moreDetailsView.uiEventObservable.subscribe(observer)
    }
/*
    private val observer: Observer<HomeUiEvent> =
        Observer { value ->
            when (value) {
                HomeUiEvent.Search -> searchSong()
                HomeUiEvent.MoreDetails -> moreDetails()
                is HomeUiEvent.OpenSongUrl -> openSongUrl()
            }
        }*/


}