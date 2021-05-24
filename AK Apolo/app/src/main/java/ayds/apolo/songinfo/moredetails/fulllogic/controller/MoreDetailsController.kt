package ayds.apolo.songinfo.moredetails.fulllogic.controller

import ayds.apolo.songinfo.home.view.HomeUiEvent
import ayds.apolo.songinfo.moredetails.fulllogic.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.fulllogic.view.MoreDetailsView
import ayds.observer.Observer


interface MoreDetailsController{
     fun setMoreDetailsView(moreDetailsView : MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
    private val moreDetailsModel : MoreDetailsModel
) : MoreDetailsController{

    private lateinit var moreDetailsView : MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView){
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